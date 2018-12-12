package idev.mysimplecalculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import idev.mysimplecalculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = 'x';
    private static final char DIVISION = '/';
    private char CURRENT_OPERATION;
    private double firstNumber, secondNumber;
    private String equation = "";
    private DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        decimalFormat = new DecimalFormat("#.##########");

        binding.button0.setOnClickListener(this);
        binding.button1.setOnClickListener(this);
        binding.button2.setOnClickListener(this);
        binding.button3.setOnClickListener(this);
        binding.button4.setOnClickListener(this);
        binding.button5.setOnClickListener(this);
        binding.button6.setOnClickListener(this);
        binding.button7.setOnClickListener(this);
        binding.button8.setOnClickListener(this);
        binding.button9.setOnClickListener(this);
        binding.buttonDot.setOnClickListener(this);
        binding.buttonCE.setOnClickListener(this);
        binding.buttonC.setOnClickListener(this);
        binding.buttonSign.setOnClickListener(this);
        binding.buttonPlus.setOnClickListener(this);
        binding.buttonMinus.setOnClickListener(this);
        binding.buttonMult.setOnClickListener(this);
        binding.buttonDiv.setOnClickListener(this);
        binding.buttonEqual.setOnClickListener(this);
        binding.buttonHist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String calculationText = binding.calculationView.getText().toString();
        switch (v.getId()) {
            case R.id.button0:
                binding.calculationView.setText(binding.calculationView.getText() + "0");
                break;

            case R.id.button1:
                binding.calculationView.setText(binding.calculationView.getText() + "1");
                break;

            case R.id.button2:
                binding.calculationView.setText(binding.calculationView.getText() + "2");
                break;

            case R.id.button3:
                binding.calculationView.setText(binding.calculationView.getText() + "3");
                break;

            case R.id.button4:
                binding.calculationView.setText(binding.calculationView.getText() + "4");
                break;

            case R.id.button5:
                binding.calculationView.setText(binding.calculationView.getText() + "5");
                break;

            case R.id.button6:
                binding.calculationView.setText(binding.calculationView.getText() + "6");
                break;

            case R.id.button7:
                binding.calculationView.setText(binding.calculationView.getText() + "7");
                break;

            case R.id.button8:
                binding.calculationView.setText(binding.calculationView.getText() + "8");
                break;

            case R.id.button9:
                binding.calculationView.setText(binding.calculationView.getText() + "9");
                break;

            case R.id.buttonDot:
                if (calculationText.length() == 0)
                    binding.calculationView.setText("0.");
                else if (!binding.calculationView.getText().toString().contains("."))
                    binding.calculationView.setText(calculationText + ".");
                break;

            case R.id.buttonCE:
                if (calculationText.length() > 0)
                    calculationText = calculationText.substring(0, calculationText.length() - 1);
                binding.calculationView.setText(calculationText);
                break;

            case R.id.buttonC:
                binding.calculationView.setText("");
                binding.resultView.setText("");
                firstNumber = 0;
                secondNumber = 0;
                break;

            case R.id.buttonSign:
                if (!calculationText.contains("-"))
                    calculationText = "-" + calculationText;
                else
                    calculationText = calculationText.substring(1, calculationText.length());
                binding.calculationView.setText(calculationText);
                break;

            case R.id.buttonPlus:
                calculate(ADDITION);
                break;

            case R.id.buttonMinus:
                calculate(SUBTRACTION);
                break;

            case R.id.buttonMult:
                calculate(MULTIPLICATION);
                break;

            case R.id.buttonDiv:
                calculate(DIVISION);
                break;

            case R.id.buttonEqual:
                if (binding.resultView.getText().toString().length() == 0) break;
                calculate(CURRENT_OPERATION);
                binding.calculationView.setText(decimalFormat.format(firstNumber));
                binding.resultView.setText("");
                equation = equation.concat(" = " + decimalFormat.format(firstNumber));
                // Store the equation in the database.
                storeEquation(equation);
                equation = "";
                break;

            case R.id.buttonHist:
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                break;
        }
    }

    private void calculate(char OPERATION) {
        String calculationText = binding.calculationView.getText().toString();
        String resultText = binding.resultView.getText().toString();
        if (calculationText.length() == 0 || (calculationText.length() == 1 && calculationText.contains("-")))
            return;
        if (resultText.length() == 0) {
            firstNumber = Double.parseDouble(calculationText);
            equation = equation.concat(decimalFormat.format(firstNumber) + " ");
        } else {
            secondNumber = Double.parseDouble(calculationText);
            equation = equation.concat(CURRENT_OPERATION + " " + decimalFormat.format(secondNumber) + " ");
            switch (CURRENT_OPERATION) {
                case ADDITION:
                    firstNumber += secondNumber;
                    break;
                case SUBTRACTION:
                    firstNumber -= secondNumber;
                    break;
                case MULTIPLICATION:
                    firstNumber *= secondNumber;
                    break;
                case DIVISION:
                    firstNumber /= secondNumber;
                    break;
            }
        }
        binding.resultView.setText(decimalFormat.format(firstNumber).concat(" " + OPERATION));
        binding.calculationView.setText("");
        CURRENT_OPERATION = OPERATION;
    }

    private void storeEquation(String equation) {
        SQLiteDatabase sqLiteDatabase = new EquationsDbHelper(this).getWritableDatabase();

        // Getting current time with yyyy / MM / dd at HH:mm:ss format.
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy / MM / dd 'at' HH:mm:ss");

        ContentValues values = new ContentValues();
        values.put(EquationsDbHelper.FeedEntry.COLUMN_EQUATION, equation);
        values.put(EquationsDbHelper.FeedEntry.COLUMN_TIME, dateFormat.format(currentTime.getTime()));

        sqLiteDatabase.insert(EquationsDbHelper.FeedEntry.TABLE_NAME, null, values);
    }
}
