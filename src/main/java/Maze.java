import javax.management.Query;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Maze extends JFrame {

    private int[][] values;
    private boolean[][] visited;
    private int startRow;
    private int startColumn;
    private ArrayList<JButton> buttonList;
    private int rows;
    private int columns;
    private boolean backtracking;
    private int algorithm;

    public Maze(int algorithm, int size, int startRow, int startColumn) {
        this.algorithm = algorithm;
        Random random = new Random();
        this.values = new int[size][];
        for (int i = 0; i < values.length; i++) {
            int[] row = new int[size];
            for (int j = 0; j < row.length; j++) {
                if (i > 1 || j > 1) {
                    row[j] = random.nextInt(8) % 7 == 0 ? Definitions.OBSTACLE : Definitions.EMPTY;
                } else {
                    row[j] = Definitions.EMPTY;
                }
            }
            values[i] = row;
        }
        values[0][0] = Definitions.EMPTY;
        values[size - 1][size - 1] = Definitions.EMPTY;
        this.visited = new boolean[this.values.length][this.values.length];
        this.startRow = startRow;
        this.startColumn = startColumn;
        this.buttonList = new ArrayList<>();
        this.rows = values.length;
        this.columns = values.length;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setLocationRelativeTo(null);
        GridLayout gridLayout = new GridLayout(rows, columns);
        this.setLayout(gridLayout);
        for (int i = 0; i < rows * columns; i++) {
            int value = values[i / rows][i % columns];
            JButton jButton = new JButton(String.valueOf(i));
            if (value == Definitions.OBSTACLE) {
                jButton.setBackground(Color.BLACK);
            } else {
                jButton.setBackground(Color.WHITE);
            }
            this.buttonList.add(jButton);
            this.add(jButton);
        }
        this.setVisible(true);
        this.setSize(Definitions.WINDOW_WIDTH, Definitions.WINDOW_HEIGHT);
        this.setResizable(false);
    }

    public void checkWayOut() {
        new Thread(() -> {
            boolean result = false;
            switch (this.algorithm) {
                case Definitions.ALGORITHM_DFS:
                    break;
                case Definitions.ALGORITHM_BFS:
                    result = bfs();
                    break;
            }
            JOptionPane.showMessageDialog(null,  result ? "FOUND SOLUTION" : "NO SOLUTION FOR THIS MAZE");

        }).start();
    }

    public boolean bfs() {
        java.util.Queue<NodeIndex> nodeQueue = new LinkedList<>();
        nodeQueue.add(new NodeIndex(this.startRow, this.startColumn));
        while (!nodeQueue.isEmpty()){
            NodeIndex currentNodeIndex = nodeQueue.remove();
            if (!isVisited(currentNodeIndex)){
                this.visited[currentNodeIndex.getRow()][currentNodeIndex.getColumn()] = true;
                setSquareAsVisited(currentNodeIndex.getRow(),currentNodeIndex.getColumn(),true);
                if (currentNodeIndex.getRow() == this.values.length - 1
                        && currentNodeIndex.getColumn() == this.values.length - 1){
                    return true;
                }
                List<NodeIndex> neighbors = getNeighbors(currentNodeIndex);
                for (NodeIndex neighbor : neighbors){
                    if (!isVisited(neighbor)){
                        nodeQueue.add(neighbor);
                    }
                }
            }
        }
        return false;
    }

    public boolean isVisited(NodeIndex nodeIndex){
        return this.visited[nodeIndex.getRow()][nodeIndex.getColumn()];
    }
    public List<NodeIndex> getNeighbors(NodeIndex currentNodeIndex){
        List<NodeIndex> neighbors = new ArrayList<>();
        neighbors.add(new NodeIndex(currentNodeIndex.getRow(), currentNodeIndex.getColumn()-1));
        neighbors.add(new NodeIndex(currentNodeIndex.getRow()+1, currentNodeIndex.getColumn()));
        neighbors.add(new NodeIndex(currentNodeIndex.getRow()-1, currentNodeIndex.getColumn()));
        neighbors.add(new NodeIndex(currentNodeIndex.getRow(), currentNodeIndex.getColumn()+1));
        checkNeighbors(neighbors);
        return neighbors;
    }

    public void checkNeighbors(List<NodeIndex>neighbors){
        List<NodeIndex> NodeToRemoveList = new ArrayList<>();
        for (NodeIndex nodeIndex : neighbors){
            if ((nodeIndex.getRow()<0) || (nodeIndex.getRow()>this.values.length-1) || (nodeIndex.getColumn()<0) ||
                    (nodeIndex.getColumn()>this.values.length-1) ||
                    (this.values[nodeIndex.getRow()][nodeIndex.getColumn()] == Definitions.OBSTACLE)){
                NodeToRemoveList.add(nodeIndex);
            }
        }
        neighbors.removeAll(NodeToRemoveList);
    }

//    public JButton[][] getButtonMatrix () {
//        int matrixLength = visited.length;
//        JButton[][] buttonMatrix = new JButton[matrixLength][matrixLength];
//        int x = 0;
//        int y = 0;
//        for (JButton currenButton : buttonList ) {
//            buttonMatrix[x][y] = currenButton;
//            x++;
//            if(x == matrixLength) {
//                y++;
//                x = 0;
//            }
//        }
//        return buttonMatrix;
//    }
//
//    public int[] buttonIndex (JButton button) {
//        int[] index = new int[2];
//        JButton[][] buttonMatrix = getButtonMatrix();
//        for (int i = 0 ; i < buttonMatrix.length; i++) {
//            for (int j = 0; j< buttonMatrix.length; j++) {
//                if (buttonMatrix[i][j].equals(button)) {
//                    index[0] = i;
//                    index[1] = j;
//                    break;
//                }
//            }
//        }
//        return index;
//    }

//    public JButton[] getNeighbor (JButton button){
//        JButton[][] buttonMatrix = getButtonMatrix();
//        JButton[] neighbors = new JButton[4];
//        int[] index = buttonIndex(button);
//        int x = index[0];
//        int y = index[1];
//        if (x==0) {
//            if (y==0) {
//                neighbors[0]=buttonMatrix[x][y+1];
//                System.out.println(x);
//                System.out.println( " ," + y);
//                neighbors[1]=buttonMatrix[x+1][y];
//            } if (y==buttonMatrix.length) {
//                neighbors[0]=buttonMatrix[x][y-1];
//                neighbors[1]=buttonMatrix[x+1][y];
//            } else {
//                neighbors[0]=buttonMatrix[y][x+1];
//                neighbors[1]=buttonMatrix[x+1][y];
//                neighbors[2]=buttonMatrix[x][y+1];
//            }
//        } if ( x==buttonMatrix.length) {
//            if (y==0) {
//                neighbors[0]=buttonMatrix[x-1][y];
//                neighbors[1]=buttonMatrix[x-1][y+1];
//            } if (y==buttonMatrix.length) {
//                neighbors[0]=buttonMatrix[y][x];
//            } else {
//                neighbors[0]=buttonMatrix[x][y+1];
//                neighbors[1]=buttonMatrix[x-1][y];
//                neighbors[2]=buttonMatrix[x][y-1];
//            }
//        } else {
//            if (y==0) {
//                neighbors[0]=buttonMatrix[x-1][y];
//                neighbors[1]=buttonMatrix[x][y+1];
//                neighbors[2]=buttonMatrix[x+1][y];
//            } if (y==buttonMatrix.length) {
//                neighbors[0]=buttonMatrix[x-1][y];
//                neighbors[1]=buttonMatrix[x][y-1];
//                neighbors[2]=buttonMatrix[x+1][y];
//            } else {
//                neighbors[0]=buttonMatrix[x][y+1];
//                neighbors[1]=buttonMatrix[x][y-1];
//                neighbors[2]=buttonMatrix[x+1][y];
//                neighbors[3]=buttonMatrix[x-1][y];
//            }
//        }
//        return neighbors;
//    }


    public void setSquareAsVisited(int x, int y, boolean visited) {
        try {
            if (visited) {
                if (this.backtracking) {
                    Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE * 5);
                    this.backtracking = false;
                }
                this.visited[x][y] = true;
                for (int i = 0; i < this.visited.length; i++) {
                    for (int j = 0; j < this.visited[i].length; j++) {
                        if (this.visited[i][j]) {
                            if (i == x && y == j) {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.RED);
                            } else {
                                this.buttonList.get(i * this.rows + j).setBackground(Color.BLUE);
                            }
                        }
                    }
                }
            } else {
                this.visited[x][y] = false;
                this.buttonList.get(x * this.columns + y).setBackground(Color.WHITE);
                Thread.sleep(Definitions.PAUSE_BEFORE_BACKTRACK);
                this.backtracking = true;
            }
            if (!visited) {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE / 4);
            } else {
                Thread.sleep(Definitions.PAUSE_BEFORE_NEXT_SQUARE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
