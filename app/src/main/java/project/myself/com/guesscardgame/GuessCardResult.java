package project.myself.com.guesscardgame;

/**
 * @类说明 猜牌数据
 */

public class GuessCardResult {
    private String result;
    private int blackNumber;
    private int whiteNumber;

    public GuessCardResult(String result, int blackNumber, int whiteNumber) {
        this.result = result;
        this.blackNumber = blackNumber;
        this.whiteNumber = whiteNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getBlackNumber() {
        return blackNumber;
    }

    public void setBlackNumber(int blackNumber) {
        this.blackNumber = blackNumber;
    }

    public int getWhiteNumber() {
        return whiteNumber;
    }

    public void setWhiteNumber(int whiteNumber) {
        this.whiteNumber = whiteNumber;
    }
}
