package client.crDetails.supervisor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import client.ClientController;
import client.ClientUI;
import client.crDetails.CrDetails;
import client.crDetails.supervisor.AssignPhaseLeaders.AssignPhaseLeaders;
import common.IcmUtils;
import entities.ChangeInitiator;
import entities.ChangeRequest;
import entities.Phase;
import entities.Phase.PhaseName;
import entities.Phase.PhaseStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import server.ServerService;
import server.ServerService.DatabaseService;

public class SupervisorButtons implements ClientUI {

	@FXML
	private Button phaseTimeDecisionButton;

	@FXML
	private Button assignPhaseLeadersButton;

	@FXML
	private Button freezeRequestButton;

	@FXML
	private Button closeChangeRequestButton;
	@FXML
	private Button phaseTimeRequestInfo;
	@FXML
	private Button moreInformation2;
	@FXML
	private Button freezeRequestInfoButton;
	@FXML
	private Button editButton;

	private static Phase.PhaseStatus CurrStatus;
    private String info;
    private ClientController clientController;
    private static Phase currPhase;
    private boolean flag=false;
    
    public void initialize() {
    	try {
			clientController=ClientController.getInstance(this);
			moreInformation2.setVisible(false);	
			phaseTimeRequestInfo.setVisible(false);

		
			if(CrDetails.getCurrRequest().getPhases().get(0).getName()!=PhaseName.CLOSING)
			{
				info="not in closing";
				closeChangeRequestButton.setDisable(true);
				moreInformation2.setVisible(true);

			}
			if (CrDetails.getCurrRequest().getPhases().get(0).getName()==PhaseName.CLOSING&&CrDetails.getCurrRequest().getPhases().get(0).getPhaseStatus()==Phase.PhaseStatus.DONE) {
				info = "finished";
				moreInformation2.setVisible(true);
				closeChangeRequestButton.setDisable(true);
				freezeRequestButton.setDisable(true);
				
			}
			
			if(flag==false) {
				currPhase=CrDetails.getCurrRequest().getPhases().get(0);
				CurrStatus = CrDetails.getCurrRequest().getPhases().get(0).getPhaseStatus();
 			    flag=true; 
			}
    		if(currPhase.getName()!=PhaseName.SUBMITTED) {
    			assignPhaseLeadersButton.setText("View phase leaders");

    		}
    		if(!(CurrStatus.equals(Phase.PhaseStatus.TIME_REQUESTED)||CurrStatus==Phase.PhaseStatus.EXTENSION_TIME_REQUESTED)){
    			phaseTimeDecisionButton.setDisable(true);
    			phaseTimeRequestInfo.setVisible(true);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    /**
     * Closes change request
     * @param event-close request button pressed
     */
	@FXML
	void closeChangeRequest(ActionEvent event) {
		Optional<ButtonType> result = IcmUtils.displayConfirmationMsg("are you sure you want to close this request?");
		if (result.get() == ButtonType.OK) {
			List<String> params = new ArrayList<String>();
			params.add(CrDetails.getCurrRequest().getId().toString());
			params.add(CrDetails.getCurrRequest().getPhases().get(0).getPhaseStatus().toString());
			params.add(CrDetails.getCurrRequest().getInitiator().getFirstName() + " "
					+ CrDetails.getCurrRequest().getInitiator().getLastName());
			params.add(CrDetails.getCurrRequest().getInitiator().getEmail());
			clientController.handleMessageFromClientUI(new ServerService(DatabaseService.Close_Request, params));
		}
	}
	/**
	 * Freezes specific request
	 * @param event-freeze button pressed
	 */
	@FXML
	void freezeRequest(ActionEvent event) {
		Optional<ButtonType> result = IcmUtils.displayConfirmationMsg("are you sure you want to freeze this request?");
		if (result.get() == ButtonType.OK) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(CrDetails.getCurrRequest().getId());
			clientController.handleMessageFromClientUI(new ServerService(DatabaseService.Freeze_Request, list));
		}
		
	}

    @FXML
    void setTimeDecision(ActionEvent event) {

    		CurrStatus = CrDetails.getCurrRequest().getPhases().get(0).getPhaseStatus();
    		System.out.println(CrDetails.getCurrRequest().getPhases().get(0).getPhaseStatus());
    		System.out.println(CurrStatus);
    		switch (CurrStatus) {
    		case TIME_REQUESTED:
    			try {
    				IcmUtils.popUpScene(this, "Time Request Decision","/client/crDetails/supervisor/timeDecision/TimeRequestDecision.fxml", 588, 688);
    				initialize();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			break;

    		case EXTENSION_TIME_REQUESTED:
    			try {
    				IcmUtils.popUpScene(this, "Time Request Decision","/client/crDetails/supervisor/timeDecision/ExtensionTimeDecision.fxml", 588, 688);
    				initialize();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			break;

		default:
			IcmUtils.displayErrorMsg("Error", "Phase time error", "There are no time requests.\n" +
					"The button should be disable.\nPlease contact system administrator.");

		}
	}

	@FXML
	private void phaseTimeDecisionInfo() {
		IcmUtils.displayInformationMsg("Phase Time Help", "Phase Time Approved", "Phase time for this phase already approved.");
	}

	@FXML
	private void freezeRequestInfo() {
		IcmUtils.displayInformationMsg("Phase Time Help", "Phase Time Approved", "Phase time for this phase already approved.");
	}

	@FXML
	void showAssignPhaseLeadersDialog(ActionEvent event) {
		try {
			IcmUtils.popUpScene(this, "Assign Phase Leaders",
					"/client/crDetails/supervisor/AssignPhaseLeaders/AssignPhaseLeaders.fxml", 588, 768);
			initialize();
		} catch (IOException e) {
			e.printStackTrace(); }
    }
	/**
	 * Displays information why close button disabled
	 */
	@FXML
	public void moreInformation2Event() {
		switch (info) {

			case "finished":
				IcmUtils.displayInformationMsg("Information message", "This request closed");
				break;
			case "not in closing":
				IcmUtils.displayInformationMsg("Information message", "This request is still not on closing phase");
				break;
		}
	}
    
    @Override
    public void handleMessageFromClientController(ServerService serverService) {
    	switch(serverService.getDatabaseService()) {
    		case Freeze_Request:
    			if((Boolean)serverService.getParams().get(0)==true){
					IcmUtils.displayInformationMsg("Success", "Freeze Request Successfully");
					freezeRequestButton.setDisable(true);
				}

    			else
    				IcmUtils.displayErrorMsg("Error", "Freeze Request Failed");
    			break;
    		case Close_Request:
				if ((Boolean) serverService.getParams().get(0) == true) {
					IcmUtils.displayInformationMsg("Close Request","Close Request", "Close Request Successfully");
					closeChangeRequestButton.setDisable(true);
					moreInformation2.setVisible(true);
					info="finished";
				}
				break;
    	}
    }

    @FXML
	private void editRequest(){

	}

	@FXML
	private void editButtonInfoMsg(){

	}
    
    public static void setCurrPhase (Phase phase) {
    	SupervisorButtons.currPhase=phase;
    }
    
    public static Phase getPhase() {
    	return currPhase;
    }
    
    public static void setCurrPhaseStatus (PhaseStatus status) {
    	SupervisorButtons.CurrStatus = status;
    }
    
    public static PhaseStatus getPhaseStatus() {
    	return CurrStatus;
    }
}
