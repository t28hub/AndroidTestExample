package com.t28.android.example.data.model;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class EntryTest {

    @Test
    public void isValid_shouldReturnTrueWhenEntryIsValid(){
        // setup
    }

    @Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2, manifest = "src/test/AndroidManifest.xml")
    @RunWith(RobolectricTestRunner.class)
    public static class BuilderTest {

        @Test
        public void constructor_shouldReturnInstance() {
            // exercise
            final Entry.Builder builder = new Entry.Builder();

            // verify
            assertThat(builder).isNotNull();
        }
    }
}
