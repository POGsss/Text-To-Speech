package com.example.texttospeech;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    // Variables Declaration
    String speechTxt, extracted;
    InputStream inputStream;
    AudioManager volume;
    TextToSpeech speaker;
    EditText input, preview;
    SeekBar speakerPitch, speakerSpeed, speakerVolume;
    Button send, upload, download, play, about;

    // On Create Or Main Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_home);

        // Variables Initialization
        input = findViewById(R.id.inputTxt);
        preview = findViewById(R.id.previewSpeech);
        send = findViewById(R.id.inputBtn);
        upload = findViewById(R.id.uploadBtn);
        download = findViewById(R.id.downloadBtn);
        play = findViewById(R.id.playBtn);
        about = findViewById(R.id.aboutBtn);
        speakerPitch = findViewById(R.id.pitch);
        speakerSpeed = findViewById(R.id.speed);
        speakerVolume = findViewById(R.id.volume);
        volume = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Setting Onclick Listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about();
            }
        });

        // Initializing Text To Speech
        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    int result = 0;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        for (Voice language : speaker.getVoices()) {
                            Log.d("consoleCheck", language.getName());
                        }
                        result = speaker.setLanguage(Locale.forLanguageTag("en"));
                    }

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("consoleCheck", "Language not supported");
                        speaker.setLanguage(Locale.US);
                    } else {
                        play.setEnabled(true);
                    }
                } else {
                    Log.d("consoleCheck", "Language not supported");
                }
            }
        });
    }

    // Different App Functionalities
    public void sendText() {
        speechTxt = input.getText().toString();
        input.setText("");

        preview.setText(speechTxt);
    }

    public void upload() {
        Intent file = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        file.addCategory(Intent.CATEGORY_OPENABLE);
        file.setType("application/pdf");

        activityResultLauncher.launch(file);
    }

    public void download() {
        final Dialog bottomSheet = new Dialog(this);
        bottomSheet.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheet.setContentView(R.layout.bottom_sheet);

        EditText fileInput = bottomSheet.findViewById(R.id.filename);
        Button saveMP3 = bottomSheet.findViewById(R.id.saveMP3);

        saveMP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = fileInput.getText().toString();
                if(!fileName.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Saving " + fileName, Toast.LENGTH_SHORT).show();
                    saveAsMP3(fileName);
                    bottomSheet.dismiss();
                } else {
                    Toast.makeText(HomeActivity.this, "Filename cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomSheet.show();
        bottomSheet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheet.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
        bottomSheet.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void speak() {
        String speakText = preview.getText().toString();
        int maxVolume = volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        speakerVolume.setMax(maxVolume);
        volume.setStreamVolume(AudioManager.STREAM_MUSIC, speakerVolume.getProgress(), 0);

        float pitch = (float) speakerPitch.getProgress() / 50;
        float speed = (float) speakerSpeed.getProgress() / 50;

        if(pitch < 0.1) {
            pitch = 0.1f;
        }
        if(speed < 0.1) {
            speed = 0.1f;
        }

        speaker.setPitch(pitch);
        speaker.setSpeechRate(speed);
        speaker.speak(speakText, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void about() {
        final Dialog bottomSheet = new Dialog(this);
        bottomSheet.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheet.setContentView(R.layout.bottom_sheet_1);

        Button doneBtn = bottomSheet.findViewById(R.id.doneBtn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.dismiss();
            }
        });

        bottomSheet.show();
        bottomSheet.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheet.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
        bottomSheet.getWindow().setGravity(Gravity.BOTTOM);
    }

    // Uploading PDF And Getting File Path
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            int resultCode = activityResult.getResultCode();
            Intent resultData = activityResult.getData();

            if(resultCode == RESULT_OK) {
                if(resultData != null) {
                    Log.d("consoleCheck", "File path: " + resultData.getData());
                    pdfToString(resultData.getData());
                } else {
                    Log.d("consoleCheck", "Result data is null.");
                }
            } else {
                Log.d("consoleCheck", "File selection cancelled.");
            }
        }
    });

    // Setting The File Path And Converting To String
    public void pdfToString(Uri uri) {
        try {
            inputStream = HomeActivity.this.getContentResolver().openInputStream(uri);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileContent = "";
                StringBuilder builder = new StringBuilder();
                PdfReader reader = null;

                try {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        reader = new PdfReader(inputStream);

                        int pages = reader.getNumberOfPages();
                        for(int i = 1; i <= pages; i++) {
                            fileContent = PdfTextExtractor.getTextFromPage(reader, i);
                            builder.append(fileContent);
                            extracted = builder.toString().replaceAll("\\r\\n|\\r|\\n", "");
                            Log.d("consoleCheck", "Page " + i);
                        }
                    }
                    reader.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            preview.setText(extracted);
                        }
                    });
                } catch(IOException e) {
                    Log.d("consoleCheck", e.getMessage());
                }
            }
        }).start();
    }

    // Saving MP3 File Locally
    private void saveAsMP3(String fileName) {
        String textToSpeech = preview.getText().toString();

        if (!TextUtils.isEmpty(textToSpeech)) {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(directory, fileName + ".mp3");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                speaker.synthesizeToFile(textToSpeech, null, file, fileName);
            }

            Toast.makeText(this, fileName + ".mp3 is successfully saved to Downloads", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, fileName + ".mp3 is empty and cannot be downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    // Text To Speech On Destroy
    @Override
    protected void onDestroy() {
        if(speaker != null) {
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }
}