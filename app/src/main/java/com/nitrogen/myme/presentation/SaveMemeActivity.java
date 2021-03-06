package com.nitrogen.myme.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nitrogen.myme.application.Main;
import com.nitrogen.myme.application.Services;
import com.nitrogen.myme.business.Exceptions.InvalidMemeException;
import com.nitrogen.myme.business.Exceptions.MemeHasDuplicateNameException;
import com.nitrogen.myme.business.Exceptions.MemeHasDuplicateTagsException;
import com.nitrogen.myme.business.Exceptions.MemeHasNoTagsException;
import com.nitrogen.myme.business.Exceptions.MemeHasNonexistentTagsException;
import com.nitrogen.myme.business.Exceptions.MemeNameTooLongException;
import com.nitrogen.myme.business.Exceptions.NamelessMemeException;
import com.nitrogen.myme.business.MemeValidator;
import com.nitrogen.myme.business.SaveHandler;
import com.nitrogen.myme.business.UpdateMemes;
import com.nitrogen.myme.objects.Meme;
import com.nitrogen.myme.objects.Tag;

import com.nitrogen.myme.R;

import java.util.ArrayList;
import java.util.List;

public class SaveMemeActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE_FILE_NAME = "com.nitrogen.myme.MESSAGE_MEME_NAME";

    public static final String INVALID_NAME = "Invalid Name";
    public static final String INVALID_NAME_NULL = "Meme must have a name";
    public static final String INVALID_NAME_LENGTH = "Length of meme name cannot be greater than ";
    public static final String INVALID_NAME_DUPLICATE = "Meme name already exists";
    public static final String INVALID_NAME_UNKNOWN ="Unknown Error: meme name is invalid";

    public static final String INVALID_TAGS = "Invalid Tags";
    public static final String INVALID_TAGS_NULL = "Meme must have at least 1 tag";
    public static final String INVALID_TAGS_DNE = "Some tags in this Meme do not exist in app";
    public static final String INVALID_TAGS_DUPLICATE = "Meme contains duplicates of the same tag";
    public static final String INVALID_TAGS_UNKNOWN = "Unknown error: Meme Tags are Invalid";

    List<CheckBox> tagCheckBoxes;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_meme);

        // Get the Intent that started this activity and extract the memeID
        Intent intent = getIntent();
        fileName = intent.getStringExtra(EXTRA_MESSAGE_FILE_NAME);

        createTagCheckboxes();

        initializeButtons();
    }

    /* createTagCheckboxes
     *
     * purpose: Create checkboxes to display the tags available to be assigned to their new meme
     *
     */
    private void createTagCheckboxes () {
        // LinearLayout, which holds dynamic checkboxes
        final LinearLayout attractedTo = findViewById(R.id.tag_list_save_meme_button);
        List<Tag> allTags = Services.getTagsPersistence().getTags();
        tagCheckBoxes = new ArrayList<>();

        // loop through all tags, creating checkboxes
        for(Tag curr : allTags) {
            final CheckBox tag = new CheckBox(this);
            tag.setText(curr.getName());
            tag.setTextSize(30);

            // only (un)check box if the user taps on the checkbox or its text
            tag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            attractedTo.addView(tag);
            tagCheckBoxes.add(tag);
        }
    }

    /* initializeButtons
     *
     * purpose: A method to assign actions to buttons that will control the following:
     *          - saving a meme
     *          - cancelling the save
     *
     */
    private void initializeButtons() {
        Button cancelButton = findViewById(R.id.cancel_save_meme_button);
        Button acceptButton = findViewById(R.id.accept_save_meme_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go back to create activity
                setResult(RESULT_CANCELED);
                finish();
            }

        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptMeme();
            }
        });
    }

    /* acceptMeme
     *
     * purpose: Validates the data the user has provided to save the meme.
     *          If a field is invalid, provide the user with a warning message.
     *          If all fields are valid, save the meme.
     *
     */
    private void acceptMeme () {
        Meme newMeme;
        EditText mEdit = findViewById(R.id.edit_text_meme_name);
        MemeValidator memeValidator = new MemeValidator();
        String name = mEdit.getText().toString();
        String picturePath;
        boolean isValid = true;


        picturePath = SaveHandler.getMemePicturePath(fileName);

        // create new Meme object
        newMeme = new Meme(name, picturePath);
        newMeme.setFavourite(true);

        // add tags based on checkboxes
        for(CheckBox tag : tagCheckBoxes) {
            if(tag.isChecked()) {
                newMeme.addTag(new Tag(tag.getText().toString()));
            }
        }

        // validate Name
        try {
            memeValidator.validateName(newMeme);
        } catch(NamelessMemeException e) {
            showErrorMsg(INVALID_NAME, INVALID_NAME_NULL);
            isValid = false;
        } catch(MemeNameTooLongException e) {
            showErrorMsg(INVALID_NAME, INVALID_NAME_LENGTH + memeValidator.MAX_NAME_LEN);
            isValid = false;
        } catch(MemeHasDuplicateNameException e) {
            showErrorMsg(INVALID_NAME, INVALID_NAME_DUPLICATE);
            isValid = false;
        } catch(InvalidMemeException e) {
            showErrorMsg(INVALID_NAME, INVALID_NAME_UNKNOWN);
            isValid = false;
        }

        // validate tags
        try {
            memeValidator.validateTags(newMeme);
        } catch(MemeHasNoTagsException e) {
            showErrorMsg(INVALID_TAGS, INVALID_TAGS_NULL);
            isValid = false;
        } catch(MemeHasNonexistentTagsException e) {
            showErrorMsg(INVALID_TAGS, INVALID_TAGS_DNE);
            isValid = false;
        } catch(MemeHasDuplicateTagsException e) {
            showErrorMsg(INVALID_TAGS, INVALID_TAGS_DUPLICATE);
            isValid = false;
        } catch(InvalidMemeException e) {
            showErrorMsg(INVALID_TAGS, INVALID_TAGS_UNKNOWN);
            isValid = false;
        }

        if(isValid) {
            // insert meme into database
            UpdateMemes memeUpdater = new UpdateMemes();
            memeUpdater.insertMeme(newMeme);

            // go to explore activity
            Intent intent = new Intent(this, FavouritesActivity.class);
            startActivity(intent);
            setResult(RESULT_OK);
            finish();
        }
    }

    private void showErrorMsg (String title, String message) {
        // open a error message dialog box
        new AlertDialog.Builder(SaveMemeActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null).create().show();
    }
}
