package project.myself.com.guesscardgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @类说明 猜牌游戏
 */

public class GuessCardActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = GuessCardActivity.class.getSimpleName();
    //牌列表
    private RecyclerView mRecyclerView;
    //结果列表
    private RecyclerView mRvResult;
    private EditText mEtGuessNumber;
    private TextView mTvGameResult;
    private Button mGameRules;
    private Button mBtnStart;
    private Button mBtnCompare;
    private Button mBtnEnd;
    private RadioGroup mRgGrade;

    private int[] images = {R.mipmap.picture1, R.mipmap.picture2, R.mipmap.picture3,
            R.mipmap.picture4, R.mipmap.picture5, R.mipmap.picture6};
    private List<Integer> imagesList;
    //猜牌中的最小数字
    private int number_min = 1;
    //猜牌中的最大数字
    private int number_max = 6;

    //默认水平为中级
    //初级水平
    private static final int PRIMARY_GRADE = 3;
    private static final int PRIMARY_TYPE = 1;
    //中级水平
    private static final int INTERMEDIATE_GRADE = 4;
    private static final int INTERMEDIATE_TYPE = 2;
    //高级水平
    private static final int ADVANCED_GRADE = 5;
    private static final int ADVANCED_TYPE = 3;
    //切换级别过程中的上一个级别类型
    private int lastType = -1;

    //猜牌中的所需要猜的长度
    private int number_need_length = INTERMEDIATE_GRADE;
    private int type = INTERMEDIATE_TYPE;
    //数据适配器
    private GuessCardAdapter adapter;
    //获得的需要猜牌的随机数集合
    private int[] randomCard;
    private Context mContext;
    //对比结果的次数
    private int compareResultCount = 0;
    //对比对齐的结果数目
    private int alignResultCount = 0;
    //共对比的次数
    private int count = 0;
    //最大可执行的次数
    private static final int MAX_COUNT = 7;
    //游戏结束
    private boolean isFinishGame = false;
    private GuessCardResultShowAdapter resultAdapter;
    //猜牌结果集合
    private List<GuessCardResult> resultList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_card);
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRvResult = (RecyclerView) findViewById(R.id.rv_result);
        mEtGuessNumber = (EditText) findViewById(R.id.et_guess_number);
        mTvGameResult = (TextView) findViewById(R.id.tv_game_result);
        mRgGrade = (RadioGroup) findViewById(R.id.rg_grade);

        mGameRules = (Button) findViewById(R.id.btn_rules);
        mGameRules.setOnClickListener(this);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStart.setText("开始游戏");
        mBtnStart.setOnClickListener(this);
        mBtnCompare = (Button) findViewById(R.id.btn_compare);
        mBtnCompare.setOnClickListener(this);
        mBtnEnd = (Button) findViewById(R.id.btn_end);
        mBtnEnd.setOnClickListener(this);

        imagesList = new ArrayList<>();

        for (int i = 0; i < number_need_length; i++) {
            imagesList.add(R.mipmap.picture0);
        }


        adapter = new GuessCardAdapter(imagesList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, number_need_length));
        mRecyclerView.setAdapter(adapter);

        resultList = new ArrayList<>();

        mRvResult.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        resultAdapter = new GuessCardResultShowAdapter(resultList);
        mRvResult.setLayoutManager(new LinearLayoutManager(this));
        mRvResult.setAdapter(resultAdapter);

        mEtGuessNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    //表示进行下一次的对比
                    compareResultCount = 0;
                    alignResultCount = 0;
                }
            }
        });

        mRgGrade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_primary:
                        //初级
                        number_need_length = PRIMARY_GRADE;
                        type = PRIMARY_TYPE;
                        break;
                    case R.id.rb_intermediate:
                        //中级
                        number_need_length = INTERMEDIATE_GRADE;
                        type = INTERMEDIATE_TYPE;
                        break;
                    case R.id.rb_advanced:
                        //高级
                        number_need_length = ADVANCED_GRADE;
                        type = ADVANCED_TYPE;
                        break;
                    default:
                        //中级
                        number_need_length = INTERMEDIATE_GRADE;
                        type = INTERMEDIATE_TYPE;
                        break;
                }
                imagesList = new ArrayList<>();
                for (int i = 0; i < number_need_length; i++) {
                    imagesList.add(R.mipmap.picture0);
                }
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, number_need_length));
                adapter.setImages(imagesList);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rules:
                //游戏规则
                startActivity(new Intent(mContext, GameRulesActivity.class));
                break;
            case R.id.btn_start:
                //开始游戏
                //下一轮游戏开始
                mBtnStart.setText("重新开始");
                if (isFinishGame)
                    isFinishGame = false;
                randomCard = randomCommon(number_min, number_max, number_need_length);
                if (resultList != null && resultList.size() > 0) {
                    resultList.clear();
                    resultAdapter.notifyDataSetChanged();
                }
                count = 0;
                mTvGameResult.setText("");

                imagesList = new ArrayList<>();
                for (int i = 0; i < randomCard.length; i++) {
                    imagesList.add(images[randomCard[i] - 1]);
                    //默认是显示后面的
                    adapter.setForwardImageAlpha(GuessCardAdapter.SHOW_CARD_BACK);
                }
                adapter.setImages(imagesList);
                lastType = type;
                break;
            //猜字
            case R.id.btn_compare:
                if (lastType != -1 && lastType != type) {
                    Toast.makeText(mContext, "游戏正在运行中，请勿中途切换游戏级别", Toast.LENGTH_SHORT).show();
                    return;
                }
                //1345   4365
                if (randomCard == null) {
                    Toast.makeText(mContext, "请先开始游戏，再执行猜牌操作", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isFinishGame) {
                    Toast.makeText(mContext, "该轮游戏结束，请重新开始下一轮", Toast.LENGTH_SHORT).show();
                    return;
                }
                String guessNumber = mEtGuessNumber.getText().toString().trim();
                if (guessNumber.length() != number_need_length) {
                    Toast.makeText(mContext, "请输入正确的猜牌数字位数", Toast.LENGTH_SHORT).show();
                    return;
                }

                //开始进行猜牌结果对比(数字不能有重复，排重)   124
                int inputNumber = Integer.parseInt(guessNumber);
                int g;
                int s;
                int b;
                int q;
                int w;

                g = inputNumber % 10;
                s = inputNumber / 10 % 10;
                int maxPositionStart = randomCard.length - number_need_length;

                //等级类型的判断和猜牌
                switch (type) {
                    case PRIMARY_TYPE:
                        //初级水平判断  三位
                        b = inputNumber / 100;
                        //重复数字的判断
                        if (g == s || g == b || s == b) {
                            Toast.makeText(mContext, "输入的数字中不能有重复数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (g > number_max || s > number_max || b > number_max) {
                            Toast.makeText(mContext, "输入的最大数字不能超过" + number_max, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //百位 0
                        if (isCardNumberContainsInputNumber(randomCard, b))
                            compareResultCount++;
                        if (randomCard[maxPositionStart] == b)
                            alignResultCount++;
                        //十位 1
                        if (isCardNumberContainsInputNumber(randomCard, s))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 1] == s)
                            alignResultCount++;
                        //个位 2
                        if (isCardNumberContainsInputNumber(randomCard, g))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 2] == g)
                            alignResultCount++;
                        break;
                    case INTERMEDIATE_TYPE:
                        //中级水平判断  四位
                        b = inputNumber / 100 % 10;
                        q = inputNumber / 1000;
                        //重复数字的判断
                        if (g == s || g == b || g == q || s == b || s == q || b == q) {
                            Toast.makeText(mContext, "输入的数字中不能有重复数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (g > number_max || s > number_max || b > number_max || q > number_max) {
                            Toast.makeText(mContext, "输入的最大数字不能超过" + number_max, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //千位 0
                        if (isCardNumberContainsInputNumber(randomCard, q))
                            compareResultCount++;
                        if (randomCard[maxPositionStart] == q)
                            alignResultCount++;
                        //百位 1
                        if (isCardNumberContainsInputNumber(randomCard, b))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 1] == b)
                            alignResultCount++;
                        //十位 2
                        if (isCardNumberContainsInputNumber(randomCard, s))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 2] == s)
                            alignResultCount++;
                        //个位 3
                        if (isCardNumberContainsInputNumber(randomCard, g))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 3] == g)
                            alignResultCount++;
                        break;

                    case ADVANCED_TYPE:
                        //高级水平判断  五位
                        b = inputNumber / 100 % 10;
                        q = inputNumber / 1000 % 10;
                        w = inputNumber / 10000;
                        //重复数字的判断
                        if (g == s || g == b || g == q || g == w || s == b || s == q || s == w || b == q || b == w || q == w) {
                            Toast.makeText(mContext, "输入的数字中不能有重复数字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (g > number_max || s > number_max || b > number_max || q > number_max || w > number_max) {
                            Toast.makeText(mContext, "输入的最大数字不能超过" + number_max, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //万位
                        if (isCardNumberContainsInputNumber(randomCard, w))
                            compareResultCount++;
                        if (randomCard[maxPositionStart] == w)
                            alignResultCount++;
                        //千位
                        if (isCardNumberContainsInputNumber(randomCard, q))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 1] == q)
                            alignResultCount++;
                        //百位
                        if (isCardNumberContainsInputNumber(randomCard, b))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 2] == b)
                            alignResultCount++;
                        //十位
                        if (isCardNumberContainsInputNumber(randomCard, s))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 3] == s)
                            alignResultCount++;
                        //个位
                        if (isCardNumberContainsInputNumber(randomCard, g))
                            compareResultCount++;
                        if (randomCard[maxPositionStart + 4] == g)
                            alignResultCount++;
                        break;
                }

                count++;

                String result = "第" + count + "次：输入数字为" + inputNumber;
                resultList.add(new GuessCardResult(result, alignResultCount, compareResultCount));
                resultAdapter = new GuessCardResultShowAdapter(resultList);
                mRvResult.setAdapter(resultAdapter);
//                resultAdapter.setData(resultList);
                //当数字全部对齐是表示猜牌游戏获得了成功（机会count<=MAX_COUNT）
                if (alignResultCount == number_need_length && count <= MAX_COUNT) {
                    mTvGameResult.setText("恭喜你，获得了猜牌游戏的胜利");
                    adapter.setForwardImageAlpha(GuessCardAdapter.SHOW_CARD_FOWARD);
                    isFinishGame = true;
                    count = 0;
                }

                if (count == MAX_COUNT && alignResultCount < number_need_length) {
                    mTvGameResult.setText("不好意思，游戏失败，请重新开始");
                    adapter.setForwardImageAlpha(GuessCardAdapter.SHOW_CARD_FOWARD);
                    isFinishGame = true;
                    count = 0;
                }
                //对比结束清空信息，等待下次输入
                mEtGuessNumber.setText("");
                break;
            case R.id.btn_end:
                //结束游戏
                onBackPressed();
                break;
        }
    }

    /**
     * 猜的牌中是否包含了你输入的文字
     *
     * @return
     */
    private boolean isCardNumberContainsInputNumber(int[] randomCard, int compareNumber) {
        for (int i = 0; i < randomCard.length; i++) {
            if (randomCard[i] == compareNumber)
                return true;
        }
        return false;
    }

    /**
     * 随机指定范围内N个不重复的数
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n   随机数个数
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }
}
