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

public class MainActivity extends AppCompatActivity {
    private String tempNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickBtn(View view) {
        EditText resultField = (EditText) findViewById(R.id.resultField);
        Button clickedBtn = (Button)findViewById(view.getId());
        String btnValue = clickedBtn.getText().toString();
        try {
            handleUserInput(btnValue, resultField);
        } catch (ArithmeticException e) {
            resultField.setText("Error");
        }
    }

    private void handleUserInput(String btnValue, EditText resultField) throws ArithmeticException {
        String resultFieldValue = resultField.getText().toString();

        if (resultFieldValue.equals("Error")) {
            resultFieldValue = "";
        }
        if ("C".equals(btnValue)) {
            resultField.setText("");
        }
        else if (".".equals(btnValue) ) {
            if(!checkIfComma(resultFieldValue)) {
                resultField.setText(resultFieldValue + btnValue);
            }
        }
        else if ("⌫".equals(btnValue) && resultFieldValue.length() > 0) {
            resultField.setText(resultFieldValue.substring(0, resultFieldValue.length() - 1));
        }
        else if ("-".equals(btnValue) && resultField.length() == 0) {
            resultField.setText("-");
        }
        else if (btnValue.matches("[0-9]")) {
            resultField.setText(resultFieldValue + btnValue);
        }
        else if (btnValue.matches("[×÷+-]") && resultFieldValue.length() > 0 && Character.toString(resultFieldValue.charAt(resultFieldValue.length() - 1)).matches("[0-9]")) {
            resultField.setText(resultFieldValue + btnValue);
        }
        else if ("=".equals(btnValue)) {
            compute(resultField);
        }
    }

    private boolean checkIfComma(String resultFieldValue) {
        String resultFieldValueRev = new StringBuilder(resultFieldValue).reverse().toString();
        Pattern r = Pattern.compile("[-]?\\d*\\.?\\d*");
        Matcher m = r.matcher(new StringBuilder(resultFieldValueRev));
        if (m.find()) {
            String numberSubstring = resultFieldValueRev.substring(m.start(), m.end());
            if ( !"".equals(numberSubstring)){
                return numberSubstring.contains(".");
            }
        }
        return true;
    }

    private void compute(EditText resultField) throws ArithmeticException {
        String exp = resultField.getText().toString();
        exp = exp.replaceAll("×", "*");
        exp = exp.replaceAll("÷", "/");
        exp = exp.replaceAll(",", ".");
        //remove lonely operator that are not followed by a number ex. 2 + 3 +
        if (exp.matches(".+[+-\\/*.]$")) {
            exp = exp.substring(0, exp.length() - 1);
        }
        Expression expression = new ExpressionBuilder(exp).build();
        double result = expression.evaluate();
        DecimalFormat f = new DecimalFormat("##.####");
        String resultVal = f.format(result);
        resultVal = resultVal.replaceAll(",", ".");
        resultField.setText(resultVal);
    }
}