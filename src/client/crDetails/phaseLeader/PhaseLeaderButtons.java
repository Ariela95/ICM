package client.crDetails.phaseLeader;

import client.ClientUI;
import client.crDetails.CrDetails;
import common.IcmUtils;
import entities.ChangeRequest;
import entities.Phase;
import entities.Phase.PhaseStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import server.ServerService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PhaseLeaderButtons implements ClientUI {

	@FXML
	private Button requestPhaseTimeButton2;
	@FXML
	private Button moreInformation;

	private static Phase currPhase;
	private long days;
	private String helpType;
	private int flag = 0;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


	/**
	 * Initialize the phase leader buttons.
	 *  set request extension time button to Disable When you can't
	 *  submit time extension request. 
	 */
	public void initialize() {
		requestPhaseTimeButton2.setDisable(true);
		moreInformation.setVisible(false);

		if (flag == 0) {
			currPhase = CrDetails.getCurrRequest().getPhases().get(0);
			flag = 1;
		}

		if (currPhase.isExtensionRequest() == false) {

			if (currPhase.getPhaseStatus() == PhaseStatus.IN_PROCESS) {
				LocalDate currDate = LocalDate.now(); // Create a date object
				LocalDate deadLine = currPhase.getDeadLine();
				days = (ChronoUnit.DAYS.between(currDate, deadLine)) + 1;
				System.out.println("Days between: " + days);

				if (!(days < 4 && days > 0) || days > 3) {
					helpType = "Time Exception";
					moreInformation.setVisible(true);
				} else
					requestPhaseTimeButton2.setDisable(false);
			}

			else if (currPhase.getPhaseStatus() == PhaseStatus.EXTENSION_TIME_REQUESTED) {
				helpType = "time extension requested";
				moreInformation.setVisible(true);
			}

			else {
				System.out.println(currPhase.getPhaseStatus().toString());
				helpType = "No deadline";
				moreInformation.setVisible(true);
			}
		}

		else {
			helpType = "Time extension already exists";
			moreInformation.setVisible(true);
		}
	}

	@FXML
	/**
	 * load the request extension time dialog when the appropriate button pressed
	 *
	 * @param event-request extension time button pressed event
	 */
	void showExtensionTimeDialog(ActionEvent event) {

		try {
			IcmUtils.popUpScene(this, "Time Extension Request",
					"/client/crDetails/phaseLeader/RequestExtensionTime/RequestExtensionTime.fxml", 588, 688);
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	/**
	 * Displays information about why request extension time button is disabled
	 * 
	 * @param event-more information button pressed event
	 */
	void moreInformationEvent(ActionEvent event) {

		switch (helpType) {
		case "Time Exception":
			IcmUtils.displayInformationMsg("Time Exception",
					"Phase Details-" + "\n" + "Change request ID: " + currPhase.getChangeRequestId() + "\n"
							+ "Current phase: " + currPhase.getName().toString() + "\n" + "Deadline: "
							+ currPhase.getDeadLine().format(formatter) + "\n" + "Time left: " + days + " days" + "\n",
					"This request can only be submitted if 3 or less days are left to complete this phase!");
			break;

		case "Time extension already exists":
			IcmUtils.displayInformationMsg("Time extension already exists",
					"Phase Details-" + "\n" + "Change request ID: " + currPhase.getChangeRequestId() + "\n"
							+ "Current phase: " + currPhase.getName().toString(),
					"A time extension request for this phase has already been submitted and aproved." + "\n"
							+ "Time extension request can only be aproved once!");
			break;
		case "time extension requested":
			IcmUtils.displayInformationMsg("Time extension requested",
					"Phase Details-" + "\n" + "Change request ID: " + currPhase.getChangeRequestId() + "\n"
							+ "Current phase: " + currPhase.getName().toString() + "\n" + "Phase status: "
							+ currPhase.getPhaseStatus(),
					"Time extension request for this phase has been forwarded to the supervisor's approval.");
			break;
		case "No deadline":
			IcmUtils.displayInformationMsg("No deadline",
					"Phase Details-" + "\n" + "Change request ID: " + currPhase.getChangeRequestId() + "\n"
							+ "Current phase: " + currPhase.getName().toString() + "\n",
					"The deadline for this phase has not been set yet.");
			break;
		}
	}

	/**
     * Sets the updated current phase into the private variable currPhase.
     * @param NewPhase -the updated current phase.
     */
	public static void setPhase(Phase NewPhase) {
		currPhase = NewPhase;
	}
	
	
	/**
     * Returns the updated current phase.
     *
     * @return the updated current phaser.
     */
	public static Phase getPhase() {
		return currPhase;
	}

	@Override
	/**
	 * handle message that came from the client controller
	 */
	public void handleMessageFromClientController(ServerService serverService) {

	}

}