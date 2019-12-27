package client.crDetails.phaseLeader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.ClientController;
import client.ClientUI;
import common.IcmUtils;
import entities.ChangeInitiator;
import entities.ChangeRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import server.ServerService;
import client.crDetails.CrDetails;


public class PhaseLeaderButtons implements ClientUI {

    @FXML
    private Button requestPhaseTimeButton2;
  
	
	
    @FXML
    void showRequestTimeDialog(ActionEvent event) {
    
    
    	 try {
             IcmUtils.popUpScene(this, "Extention Time Request", "/client/crDetails/phaseLeader/RequestExtensionTime/RequestExtensionTime.fxml",622 ,470 );
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    @Override
    public void handleMessageFromClientController(ServerService serverService) {
    	
 
}
    
}
