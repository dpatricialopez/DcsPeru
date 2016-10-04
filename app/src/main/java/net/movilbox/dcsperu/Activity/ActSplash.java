package net.movilbox.dcsperu.Activity;


import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;

import net.movilbox.dcsperu.DataBase.DBHelper;
import net.movilbox.dcsperu.Entry.ConfigSplash;
import net.movilbox.dcsperu.R;
import net.movilbox.dcsperu.cnst.Flags;

public class ActSplash extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {

        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.logowhite); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.DropOut); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Title
        configSplash.setTitleSplash("");
        configSplash.setTitleTextColor(R.color.actionBarColorText);
        configSplash.setTitleTextSize(19f); //float value
        configSplash.setAnimTitleDuration(3000);
        configSplash.setAnimTitleTechnique(Techniques.Tada);


    }

    @Override
    public void animationsFinished() {

        DBHelper mydb = new DBHelper(this);
        mydb.insertIntro("Inicio_sesion");

        startActivity(new Intent(this, ActLoginUser.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();

    }

}
