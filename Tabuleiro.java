import javax.swing.*; // Importa os componentes da interface gráfica do Swing
import java.awt.*; // Importa as classes para trabalhar com gráficos e layouts
import java.awt.event.*; // Importa os eventos de interação com o usuário
import java.util.ArrayList; // Importa a classe para usar listas dinâmicas
import java.util.Random; // Importa a classe para gerar números aleatórios

public class Tabuleiro extends JFrame {
    // Declaração das variáveis que representam os elementos do jogo
    private JPanel painel; // Painel onde o jogo será desenhado
    private JPanel menu; // Painel que contém os botões e o placar
    private JButton iniciarButton; // Botão para iniciar o jogo
    private JButton resetButton; // Botão para reiniciar o jogo
    private JButton pauseButton; // Botão para pausar o jogo
    private JTextField placarField; // Campo que mostra o placar
    private ArrayList<Quadrado> cobra; // Lista de quadrados representando a cobra
    private Quadrado obstaculo; // Quadrado que representa o obstáculo no jogo
    private Quadrado macaVerde; // Quadrado que representa a maçã verde
    private int larguraTabuleiro = 400; // Largura do tabuleiro
    private int alturaTabuleiro = 400; // Altura do tabuleiro
    private int placar = 0; // Placar do jogador
    private String direcao = "direita"; // Direção inicial da cobra
    private int dificuldade = 100; // Velocidade do jogo (menor valor = mais rápido)
    private boolean jogoEmAndamento = false; // Flag que indica se o jogo está em andamento
    private boolean pausado = false; // Flag para verificar se o jogo está pausado
    private Timer timer; // Timer que controla o movimento da cobra
    private int modoJogo = 1; // Modo do jogo (1 ou 2)
    private boolean controlesInvertidos = false; // Controle de direção invertida

    // Construtor que configura a interface gráfica
    public Tabuleiro() {
        iniciarUI();
    }

    // Função que inicializa a interface gráfica
    private void iniciarUI() {
        // Configurações da janela
        setTitle("Jogo da Cobrinha");
        setSize(larguraTabuleiro + 200, alturaTabuleiro + 100); // Tamanho da janela, incluindo colunas laterais
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Configuração do menu com os botões
        menu = new JPanel(new FlowLayout());
        iniciarButton = new JButton("Iniciar");
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        placarField = new JTextField("Placar: 0", 10); // Exibe o placar
        placarField.setEditable(false);

        // ComboBox para escolher o modo de jogo (1 ou 2)
        String[] opcoes = {"Modo 1 (Colidir)", "Modo 2 (Ressurgir)"};
        JComboBox<String> modoJogoBox = new JComboBox<>(opcoes);
        modoJogoBox.addActionListener(e -> modoJogo = modoJogoBox.getSelectedIndex() + 1);

        // Adiciona os componentes ao painel de menu
        menu.add(modoJogoBox);
        menu.add(iniciarButton);
        menu.add(resetButton);
        menu.add(pauseButton);
        menu.add(placarField);

        // Painel onde o jogo será desenhado
        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK); // Cor do contorno do tabuleiro
                g.drawRect(0, 0, larguraTabuleiro - 1, alturaTabuleiro - 1);

                // Desenha a cobra, o obstáculo e a maçã verde
                if (cobra != null) {
                    for (Quadrado segmento : cobra) {
                        segmento.desenhar(g);
                    }
                }
                if (obstaculo != null) {
                    obstaculo.desenhar(g);
                }
                if (macaVerde != null) {
                    macaVerde.desenhar(g);
                }
            }
        };

        painel.setPreferredSize(new Dimension(larguraTabuleiro, alturaTabuleiro));

        // Painel pai que centraliza o painel do jogo
        JPanel painelCentralizado = new JPanel(new GridBagLayout());
        painelCentralizado.setBackground(Color.BLACK); // Fundo preto para criar as colunas laterais
        painelCentralizado.add(painel);

        add(menu, BorderLayout.NORTH);
        add(painelCentralizado, BorderLayout.CENTER);
        setVisible(true);

        // Ações para os botões
        iniciarButton.addActionListener(e -> iniciarJogo());
        resetButton.addActionListener(e -> reiniciarJogo());
        pauseButton.addActionListener(e -> pausarJogo());

        painel.setFocusable(true);
        painel.requestFocusInWindow(); // Garante que o painel tenha foco para detectar as teclas
        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!pausado) { // Apenas processa os controles quando o jogo não está pausado
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A -> direcao = controlesInvertidos ? "direita" : "esquerda";
                        case KeyEvent.VK_D -> direcao = controlesInvertidos ? "esquerda" : "direita";
                        case KeyEvent.VK_W -> direcao = controlesInvertidos ? "baixo" : "cima";
                        case KeyEvent.VK_S -> direcao = controlesInvertidos ? "cima" : "baixo";
                    }
                }
            }
        });
    }

    // Função para iniciar o jogo
    private void iniciarJogo() {
        cobra = new ArrayList<>();
        cobra.add(new Quadrado(larguraTabuleiro / 2, alturaTabuleiro / 2, 10, 10, Color.BLACK)); // Inicializa a cobra no meio
        direcao = "direita"; // Direção inicial da cobra
        placar = 0; // Zera o placar
        dificuldade = 100; // Dificuldade inicial (tempo de delay do timer)
        controlesInvertidos = false; // Controles não invertidos no início
        placarField.setText("Placar: 0");
        criarObstaculo(); // Cria um obstáculo aleatório
        macaVerde = null; // Não há maçã verde no início

        jogoEmAndamento = true; // O jogo está em andamento
        pausado = false; // O jogo não está pausado

        if (timer != null) timer.stop();
        timer = new Timer(dificuldade, e -> atualizarJogo());
        timer.start(); // Inicia o timer que atualiza o jogo periodicamente

        painel.requestFocusInWindow(); // Garante que o foco seja restaurado quando o jogo iniciar
    }

    // Função para aumentar a pontuação e atualizar o placar
    private void aumentarPontuacao() {
        placar++;
        placarField.setText("Placar: " + placar);
    }

    // Função para criar um obstáculo aleatório
    private void criarObstaculo() {
        Random random = new Random();
        int x = random.nextInt(larguraTabuleiro / 10) * 10;
        int y = random.nextInt(alturaTabuleiro / 10) * 10;
        obstaculo = new Quadrado(x, y, 10, 10, Color.RED); // Obstáculo vermelho
    }

    // Função para reiniciar o jogo
    private void reiniciarJogo() {
        iniciarJogo(); // Chama a função de iniciar o jogo
    }

    // Função para pausar o jogo
    private void pausarJogo() {
        if (jogoEmAndamento) {
            pausado = !pausado;
            if (pausado) {
                timer.stop(); // Para o timer
            } else {
                timer.start(); // Reinicia o timer
                painel.requestFocusInWindow(); // Garante que o foco seja restaurado ao despausar
            }
        }
    }

    // Função que atualiza o estado do jogo
    private void atualizarJogo() {
        if (!jogoEmAndamento || pausado) return;

        moverCobra(); // Move a cobra
        checarColisao(); // Checa se houve colisão
        painel.repaint(); // Atualiza a interface gráfica
    }

    // Função que move a cobra conforme a direção
    private void moverCobra() {
        Quadrado cabeca = cobra.get(0);
        int novoX = cabeca.x;
        int novoY = cabeca.y;

        switch (direcao) {
            case "esquerda" -> novoX -= 10;
            case "direita" -> novoX += 10;
            case "cima" -> novoY -= 10;
            case "baixo" -> novoY += 10;
        }

        // Move cada segmento da cobra para a posição do anterior
        for (int i = cobra.size() - 1; i > 0; i--) {
            cobra.get(i).x = cobra.get(i - 1).x;
            cobra.get(i).y = cobra.get(i - 1).y;
        }

        // Atualiza a posição da cabeça da cobra
        cobra.get(0).x = novoX;
        cobra.get(0).y = novoY;

        // Verifica se a cobra colidiu com o obstáculo
        if (cabeca.x == obstaculo.x && cabeca.y == obstaculo.y) {
            aumentarPontuacao();
            criarObstaculo(); // Cria um novo obstáculo
            cobra.add(new Quadrado(-10, -10, 10, 10, Color.BLACK)); // Adiciona um segmento à cobra
            dificuldade = Math.max(20, dificuldade - 5); // Aumenta a velocidade do jogo
            timer.setDelay(dificuldade);
            controlesInvertidos = false; // Reseta o controle invertido
        }
        // Verifica se a cobra pegou a maçã verde
        else if (macaVerde != null && cabeca.x == macaVerde.x && cabeca.y == macaVerde.y) {
            macaVerde = null;
            if (cobra.size() > 1) {
                cobra.remove(cobra.size() - 1); // Remove o último segmento da cobra
            }
            controlesInvertidos = true; // Inverte os controles
        }
    }

    // Função para gerar a maçã verde em uma posição aleatória
    private void gerarMacaVerde() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(larguraTabuleiro / 10) * 10;
            y = random.nextInt(alturaTabuleiro / 10) * 10;
        } while (colidiuComCobra(x, y) || (obstaculo != null && obstaculo.x == x && obstaculo.y == y));
        macaVerde = new Quadrado(x, y, 10, 10, Color.GREEN); // Maçã verde
    }

    // Função que verifica se a posição da maçã colide com a cobra
    private boolean colidiuComCobra(int x, int y) {
        for (Quadrado segmento : cobra) {
            if (segmento.x == x && segmento.y == y) {
                return true;
            }
        }
        return false;
    }

    // Função que verifica colisões com as paredes e com a própria cobra
    private void checarColisao() {
        Quadrado cabeca = cobra.get(0);

        if (modoJogo == 1) {
            if (cabeca.x < 0 || cabeca.x >= larguraTabuleiro || cabeca.y < 0 || cabeca.y >= alturaTabuleiro) {
                gameOver(); // Fim de jogo se a cobra bater nas paredes
            }
        } else if (modoJogo == 2) {
            if (cabeca.x < 0) cabeca.x = larguraTabuleiro - 10;
            if (cabeca.x >= larguraTabuleiro) cabeca.x = 0;
            if (cabeca.y < 0) cabeca.y = alturaTabuleiro - 10;
            if (cabeca.y >= alturaTabuleiro) cabeca.y = 0; // Se o modo for "Ressurgir", a cobra reaparece do outro lado
        }

        // Verifica se a cabeça da cobra colidiu com o corpo
        for (int i = 1; i < cobra.size(); i++) {
            if (cabeca.x == cobra.get(i).x && cabeca.y == cobra.get(i).y) {
                gameOver(); // Fim de jogo se a cobra bater no corpo
            }
        }
    }

    // Função que finaliza o jogo e exibe a pontuação final
    private void gameOver() {
        jogoEmAndamento = false; // Define que o jogo não está mais em andamento
        timer.stop(); // Para o timer
        JOptionPane.showMessageDialog(this, "Game Over! Placar: " + placar); // Exibe uma mensagem de fim de jogo
    }

    // Função principal que inicia o jogo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Tabuleiro::new); // Cria a instância do jogo na thread de eventos do Swing
    }
}
