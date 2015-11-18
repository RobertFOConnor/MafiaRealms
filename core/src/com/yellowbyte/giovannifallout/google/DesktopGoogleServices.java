package com.yellowbyte.giovannifallout.google;

public class DesktopGoogleServices implements IGoogleServices
{
@Override
public void signIn()
{
System.out.println("DesktopGoogleServies: signIn()");
}

@Override
public void signOut()
{
System.out.println("DesktopGoogleServies: signOut()");
}

@Override
public void rateGame()
{
System.out.println("DesktopGoogleServices: rateGame()");
}

@Override
public void submitScore(long score)
{
System.out.println("DesktopGoogleServies: submitScore(" + score + ")");
}

@Override
public void showScores()
{
System.out.println("DesktopGoogleServies: showScores()");
}

@Override
public boolean isSignedIn()
{
System.out.println("DesktopGoogleServies: isSignedIn()");
return false;
}

@Override
public void unlockAchievementGPGS(String achievementId) {
	// TODO Auto-generated method stub
	
}

@Override
public void getAchievementsGPGS() {
	// TODO Auto-generated method stub
	
}

@Override
public void showAds(boolean show) {
	// TODO Auto-generated method stub
	
}
}
