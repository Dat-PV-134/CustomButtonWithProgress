## Preview

![alt text](https://github.com/Dat-PV-134/CustomButtonWithProgress/blob/main/preview.png)

## Implementation:
Project setting gradle:

```sh
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
}
```

Build.gradle (Module app):

```sh
  implementation("com.github.Dat-PV-134:CustomButtonWithProgress:1.0.2")
```

## Usage:

```sh
 <com.rekoj134.circlebuttonwithprogress.CustomButtonWithProgress
            android:id="@+id/btnNext"
            android:layout_width="@dimen/_70sdp"
            android:layout_height="@dimen/_70sdp"
            android:layout_marginEnd="@dimen/_16sdp"
            android:layout_marginBottom="@dimen/_26sdp"
            app:start_color_progress="@color/gradient_start_color"
            app:end_color_progress="@color/gradient_end_color"
            app:start_color_track="#1AFECDD3"
            app:end_color_track="#1AFECDD3"
            app:src_center="@drawable/ic_next_onboarding"
            app:total_step="3"
            app:src_center_padding="6dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />
```


