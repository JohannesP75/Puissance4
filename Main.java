import java.util.*;

public class Main {
    /***
     * Taille des lignes du plateau, le nombre de colonnes
     * */
    final static int ROW_SIZE=7;
    /**
     * Taille des colonnes du plateau, le nombre de lignes
     */
    final static int COL_SIZE=6;
    /**
     * Nombre de cellules vides
     */
    static int emptyCells;
    /**
     * Plateau de jeu (origine commençant à la case 0 de la line 0)
     */
    static char[][] board=new char[COL_SIZE][ROW_SIZE];
    /**
     * Scores des joueurs (score_1 pour joueur 1, score_2 pour le 2)
     */
    static int score_1, score_2;
    /**
     * Jetons des joueurs (jeton_1 pour joueur 1, jeton_2 pour le 2)
     */
    static char jeton_1, jeton_2;
    /**
     * Initialise le jeu
     */
    static void initGame(){
        // Initialisation des scores et jetons
        jeton_1='X';
        jeton_2='V';
        emptyCells=COL_SIZE*ROW_SIZE;

        // Initialisation du tableau
        for(int i=0;i<COL_SIZE;i++)
            Arrays.fill(board[i], '0');
    }

    /**
     * Affiche le tableau de jeu
     */
    static void afficherPlateau(){
        for(int i=0;i<COL_SIZE;i++) {
            for (int j = 0; j < ROW_SIZE; j++) {
                System.out.format("|%c|", board[i][j]);
            }

            System.out.println();
        }
    }

    /**
     * Place le jeton sur le plateau à la col indiquée
     * @param joueur Jeton du joueur
     * @param col Colonne dans laquelle il faut mettre le jeton (entre 1 et ROW_SIZE)
     * @return Ordonnée de la position du nouveau jeton (-1 si aucun jeton n'a pu être placé)
     */
    static int placerJeton(char joueur, int col){
        // Verifier si la colonne est bien libre
        int S=-1;

        for(int i=COL_SIZE-1;i>=0;i--)
            if(board[i][col-1]=='0'){
                board[i][col-1]=joueur;
                S=i;
                emptyCells--;
                break;
            }

        return S;
    }

    /**
     * Gestion du jeu
     * @return Numéro du gagnant (0 pour match nul, 1 pour joueur 1 et 2 pour joueur 2)
     */
    public static int gestionTour(){
        /**
         * Gagnant (0 pour nul, 1 pour joueur 1 et 2 pour joueur 2)
         */
        int winner=0, tour=1, choixCol, x, y;
        Scanner lectureClavier=new Scanner(System.in);
        afficherPlateau();

        while(true){
            System.out.format("\tTour %d\n", tour);

            // Joueur 1
            System.out.format("Au tour du joueur 1 (%c)\n", jeton_1);
            do {
                System.out.format("Joueur 1, entrez le numéro de la colone (de 1 a %d) ou placer le jeton : \n", ROW_SIZE);
                choixCol=lectureClavier.nextInt();
                y=(choixCol>=1&&choixCol<=ROW_SIZE)?placerJeton(jeton_1, choixCol):-1;
            }while(y==-1);

            x=choixCol-1;
            if(checkVictory(x,y)){
                winner=1;
                break;
            }

            afficherPlateau();
            if(emptyCells<=0)break;

            // Joueur 2
            System.out.format("Au tour du joueur 2 (%c)\n", jeton_2);
            do {
                System.out.format("Joueur 2, entrez le numéro de la colone (de 1 a %d) ou placer le jeton : \n", ROW_SIZE);
                choixCol=lectureClavier.nextInt();
                y=(choixCol>=1&&choixCol<=ROW_SIZE)?placerJeton(jeton_2, choixCol):-1;
            }while(y==-1);

            x=choixCol-1;
            if(checkVictory(x,y)){
                winner=2;
                break;
            }
            afficherPlateau();
            if(emptyCells<=0)break;

            tour++;
        }

        return winner;
    }

    /**
     * Détermine si l'ajout du jeton à un certaine position entraine la fin du jeu
     * @param x Abscisse du jeton sur le plateau
     * @param y Ordonnée du jeton sur le plateau
     * @return Un booléen indiquant si le jeu est fini (true) ou non (false)
     */
    public static boolean checkVictory(int x, int y){
        boolean S=false;
        char token=board[y][x];

        // Vérifications verticales
        int total=1;
        int upperLimit=Math.max(0, y-3), lowerLimit=Math.min(COL_SIZE-1, y+3);
        for(int i=y;i>=upperLimit;i--)
            if(board[i][x]==token&&i!=y)
                total++;
            else if(board[i][x]!=token)
                break;

        for(int i=y;i<=lowerLimit;i++)
            if(board[i][x]==token&&i!=y)
                total++;
            else if(board[i][x]!=token)
                break;

        S=total>=4;
        if(!S){
            // Texts horizontaux
            int leftLimit=Math.max(0, x-3), rightLimit=Math.min(ROW_SIZE-1, x+3);
            total=1;

            for(int i=x;i>=leftLimit;i--)
                if(board[y][i]==token&&i!=x)
                    total++;
                else if(board[y][i]!=token)
                    break;

            for(int i=x;i<=rightLimit;i++)
                if(board[y][i]==token&&i!=x)
                    total++;
                else if(board[y][i]!=token)
                    break;

            S=total>=4;

            if(!S){
                // Tests diagonaux
                // Diagonale NO-SE
                total=1;
                int diffNO=Math.min(x-leftLimit, y-upperLimit), diffSE=Math.min(rightLimit-x, lowerLimit-y);

                for(int i=0;i<=diffNO;i++)
                    if(board[y-i][x-i]==token&&(x-i)!=x&&(y-i)!=y)
                        total++;
                    else if(board[y-i][x-i]!=token)
                        break;

                for(int i=0;i<=diffSE;i++)
                    if(board[y+i][x+i]==token&&(x+i)!=x&&(y+i)!=y)
                        total++;
                    else if(board[y+i][x+i]!=token)
                        break;

                S=total>=4;

                if(!S){
                    // Diagonale NE-SO
                    total=1;
                    int diffNE=Math.min(rightLimit-x, upperLimit-y), diffSO=Math.min(x-leftLimit, lowerLimit-y);

                    for(int i=0;i<=diffNE;i++)
                        if(board[y-i][x+i]==token&&(x+i)!=x&&(y-i)!=y)
                            total++;
                        else if(board[y-i][x+i]!=token)
                            break;

                    for(int i=0;i<=diffSO;i++)
                        if(board[y+i][x-i]==token&&(x-i)!=x&&(y+i)!=y)
                            total++;
                        else if(board[y+i][x-i]!=token)
                            break;

                    S=total>=4;
                }
            }
        }

        return S;
    }
    public static void main(String[] args) {
        Scanner lectureClavier=new Scanner(System.in);
        score_1=0;
        score_2=0;

        while(true) {
            initGame();
            System.out.println("Statistiques de victoire :");
            System.out.println("Joueur 1 : "+score_1);
            System.out.println("Joueur 2 : "+score_2);
            int win = gestionTour();

            if (win == 0) {
                System.out.println("Match nul !!!");
            } else {
                System.out.format("Joueur %d a gagne !!!\n", win);

                if (win == 1) score_1++;
                else score_2++;
            }
            char choix;

            do {
                System.out.println("Voulez-vous continuer de jouer (O/N) ?");
                choix = Character.toUpperCase(lectureClavier.next().charAt(0));
            }while(choix!='O'&&choix!='N');

            if(choix=='N')break;
        }
    }

    public static void test(){
        afficherPlateau();
        System.out.println("Hauteur du jeton : "+placerJeton(jeton_1, 1));
        System.out.println("**********************");
        afficherPlateau();
        System.out.println("Hauteur du jeton : "+placerJeton(jeton_1, 1));
        System.out.println("**********************");
        afficherPlateau();
        System.out.println("Hauteur du jeton : "+placerJeton(jeton_2, 2));
        System.out.println("**********************");
        afficherPlateau();
    }
}