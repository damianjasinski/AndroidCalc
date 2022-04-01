package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancedCalcActivity extends AppCompatActivity {
    private EditText resultField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calc);
        resultField = findViewById(R.id.resultField);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("resultField", resultField.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        String resultFieldVal = inState.getString("resultField");
        if (resultFieldVal != null) {
            resultField.setText(resultFieldVal);
        }
    }

    public void onClickBtn(View view) {
        EditText resultField = (EditText) findViewById(R.id.resultField);
        Button clickedBtn = (Button)findViewById(view.getId());
        String btnValue = clickedBtn.getText().toString();
        try {
            handleUserInput(btnValue);
        } catch (ArithmeticException | IllegalArgumentException e) {
            resultField.setText("Error");
        }
    }

    private void handleUserInput(String btnValue) throws ArithmeticException {
        String exp = resultField.getText().toString();

        if (exp.equals("Error")) {
            exp = "";
        }
        if ("C".equals(btnValue)) {
            resultField.setText("");
        }
        else if (".".equals(btnValue) ) {
            if(!checkIfComma()) {
                resultField.setText(exp + btnValue);
            }
        }
        else if (btnValue.matches("sin|ln|cos|tan")) {
            resultField.setText(exp + btnValue + "(");
        }

        else if ("log".equals(btnValue)) {
            resultField.setText(exp + btnValue + "(");
        }
        else if (")".equals(btnValue)) {
            long countRightBraces = exp.chars()
                    .filter(ch -> ch == '(')
                    .count();
            long countLeftBraces = exp.chars()
                    .filter(ch -> ch == ')')
                    .count();
            if ( countLeftBraces < countRightBraces) {
                resultField.setText(exp + btnValue);
            }
        }
        else if ("x²".equals(btnValue) && exp.length() > 0) {
            resultField.setText(exp + "^(2)");
        }
        else if ("%".equals(btnValue) ) {
            resultField.setText(exp + "%");
        }
        else if ("√".equals(btnValue)) {
            resultField.setText(exp + "sqrt(");
        }
        else if ("xʸ".equals(btnValue) && exp.length() > 0) {
            resultField.setText(exp + "^(");
        }
        else if ("⌫".equals(btnValue) && exp.length() > 0) {
            resultField.setText(exp.substring(0, exp.length() - 1));
        }
        else if ("-".equals(btnValue) && resultField.length() == 0) {
            resultField.setText("-");
        }
        else if (("-".equals(btnValue) && Character.toString(exp.charAt(exp.length() - 1)).matches("[×÷]"))) {
            resultField.setText(exp + btnValue);
        }
        else if (btnValue.matches("[0-9]")) {
            resultField.setText(exp + btnValue);
        }
        else if (btnValue.matches("[×÷+-]") && exp.length() > 0 && Character.toString(exp.charAt(exp.length() - 1)).matches("[0-9|sin|cos|tan|log|ln|(|)]"))  {
            resultField.setText(exp + btnValue);
        }
        else if ("=".equals(btnValue)) {
            compute();
        }
    }

    // check if comma can be placed (only one comma in each number)
    private boolean checkIfComma() {
        String resultFieldVal = resultField.getText().toString();
        String resultFieldValRev = new StringBuilder(resultFieldVal).reverse().toString();
        Pattern r = Pattern.compile("[-]?\\d*\\.?\\d*");
        Matcher m = r.matcher(new StringBuilder(resultFieldValRev));
        if (m.find()) {
            String numberSubstring = resultFieldValRev.substring(m.start(), m.end());
            if ( !"".equals(numberSubstring)){
                return numberSubstring.contains(".");
            }
        }
        return true;
    }

    private void compute() throws ArithmeticException, IllegalArgumentException {
        String exp = insertMissingBracers();
        exp = exp.replaceAll("×", "*");
        exp = exp.replaceAll("÷", "/");
        exp = exp.replaceAll(",", ".");
        exp = exp.replaceAll("log", "log10");
        exp = exp.replaceAll("ln", "log");
        //remove lonely operator that are not followed by a number ex. 2 + 3 +
        if (exp.matches(".+[+-\\/*.]$")) {
            exp = exp.substring(0, exp.length() - 1);
        }
        //insert missing bracers

        Expression expression = new ExpressionBuilder(exp).build();
        double result = expression.evaluate();
        DecimalFormat f = new DecimalFormat("##.######");
        String resultVal = f.format(result);
        resultVal = resultVal.replaceAll(",", ".");
        resultField.setText(resultVal);
    }

    private String insertMissingBracers() {
        String resultFieldVal = resultField.getText().toString();
        long countRightBraces = resultFieldVal.chars()
                .filter(ch -> ch == '(')
                .count();
        long countLeftBraces = resultFieldVal.chars()
                .filter(ch -> ch == ')')
                .count();
        while (countLeftBraces != countRightBraces) {
            resultFieldVal += ")";
            countRightBraces = resultFieldVal.chars()
                    .filter(ch -> ch == '(')
                    .count();
            countLeftBraces = resultFieldVal.chars()
                    .filter(ch -> ch == ')')
                    .count();
        }
        return resultFieldVal;
    }
}