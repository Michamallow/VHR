package com.example.vhr.ImportScanSave;
import com.example.vhr.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PopupDialog extends Dialog {
    private final OnOptionSelectedListener optionSelectedListener;

    public PopupDialog(Context context, OnOptionSelectedListener optionSelectedListener) {
        super(context);
        this.optionSelectedListener = optionSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_scan_import_layout);

        EditText fileNameEditText = findViewById(R.id.fileNameEditText);
        RadioGroup radioOptions = findViewById(R.id.radioOptions);
        RadioButton scanRadioButton = findViewById(R.id.takePhotoRadioButton);
        RadioButton importRadioButton = findViewById(R.id.importFileRadioButton);
        Button confirmButton = findViewById(R.id.saveButton);

        confirmButton.setOnClickListener(view -> {
            String fileName = fileNameEditText.getText().toString();
            int selectedOptionId = radioOptions.getCheckedRadioButtonId();

            if (selectedOptionId == scanRadioButton.getId()) {
                optionSelectedListener.onOptionSelected(fileName, Option.SCAN);
            } else if (selectedOptionId == importRadioButton.getId()) {
                optionSelectedListener.onOptionSelected(fileName, Option.IMPORT);
            }

            dismiss();
        });
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected(String fileName, Option option);
    }

    public enum Option {
        SCAN,
        IMPORT
    }
}