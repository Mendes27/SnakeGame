import java.awt.Color; // Importa a classe Color para definir cores
import java.awt.Graphics; // Importa a classe Graphics para desenhar objetos gráficos

// Classe que representa um quadrado, que pode ser um segmento da cobra ou uma maçã
public class Quadrado {
    int x, y, largura, altura; // Coordenadas (x, y) e dimensões (largura, altura) do quadrado
    Color cor; // Cor do quadrado

    // Construtor que define a posição, tamanho e cor do quadrado
    public Quadrado(int x, int y, int largura, int altura, Color cor) {
        this.x = x; // Atribui o valor de x para a posição horizontal
        this.y = y; // Atribui o valor de y para a posição vertical
        this.largura = largura; // Atribui o valor da largura do quadrado
        this.altura = altura; // Atribui o valor da altura do quadrado
        this.cor = cor; // Define a cor do quadrado
    }

    // Método para desenhar o quadrado na tela
    public void desenhar(Graphics g) {
        g.setColor(cor); // Define a cor do gráfico com a cor do quadrado
        g.fillRect(x, y, largura, altura); // Preenche um retângulo na posição (x, y) com as dimensões largura e altura
    }
}
