package com.java.project.tournamenttracker.threads;

import com.java.project.tournamenttracker.entities.User;
import com.java.project.tournamenttracker.main.LoginController;
import com.java.project.tournamenttracker.main.MainController;

public class LoggedUserThread implements Runnable{
    @Override
    public synchronized void run() {
        User user = LoginController.getLoggedUser();
        if (user != null){
            MainController.getStage().setTitle("Logged in as: " + user.getUsername() + " (" + user.getRole() + ")");
        }
        else {
            MainController.getStage().setTitle("Not logged in!");
        }
    }
}
