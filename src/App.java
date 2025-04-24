import javax.swing.*; //Importa a biblioteca javax.swing, que é a responsável por criar interfaces gráficas em Java — como janelas, botões, painéis, etc.

public class App { //Classe App nomeada

    //Função principal onde o Java começa a execução do programa:

    /*public → Acessível por qualquer parte do programa;
    static → Pode ser executado sem precisar instanciar a classe;
    void → Não retorna nenhum valor;
    String[] args → Permite receber argumentos pela linha de comando (não usados aqui)
    throws Exception → Só diz que pode lançar exceções (erros) — útil em códigos gráficos ou com arquivos, etc.*/
    public static void main(String[] args) throws Exception { 

        //Criação da janela do jogo Flappy Bird
        int LarguraBorda = GameConstants.LARGURA_BORDA; //Largura da borda de 360px
        int AlturaBorda = GameConstants.ALTURA_BORDA; //Altura da borda de 640px

        JFrame janela = new JFrame("Flappy Bird"); //Cria um novo objeto/janela da interface gráfica. O texto "Flappy Bird" aparece como título da janela.
        janela.setVisible(true); //Faz a janela visível.
        janela.setSize(LarguraBorda, AlturaBorda); //Define o tamanho da janela, usando os valores definidos acima.
        janela.setLocationRelativeTo(null); //Centraliza a janela na tela.
        janela.setResizable(false); //Impede que o usuário redimensione a janela.
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Define o que acontece quando o usuário fecha a janela (encerra o programa).

        FlappyBird flappyBird = new FlappyBird(); //com essa linha, você tá preparando o seu painel de jogo, que é onde tudo vai acontecer (desenhos, física, etc).
        janela.add(flappyBird); //Adiciona o painel de jogo à janela.
        janela.pack(); //Ajusta o tamanho da janela para que o painel de jogo se ajuste perfeitamente.
        janela.setVisible(true); //Faz a janela visível.
    }
}
