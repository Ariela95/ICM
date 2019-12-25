package client.crDetails;


import client.ClientController;
import client.ClientUI;
import client.crDetails.ccc.CCCButtons;
import common.IcmUtils;
import entities.ChangeInitiator;
import entities.ChangeRequest;
import entities.IEPhasePosition;
import entities.InformationEngineer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import server.ServerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrDetails implements ClientUI {

    @FXML
    private TextField changeRequestIDTextField;
    @FXML
    private TextField openingDateTextField;
    @FXML
    private TextField initiatorTextField;
    @FXML
    private TextField infoSystemTextField;
    @FXML
    private TextArea currentStateTextArea;
    @FXML
    private TextArea requestedChangeTextField;
    @FXML
    private TextArea reasonForChangeTextArea;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private TextField currentPhaseTextField;
    @FXML
    private TextField phaseDeadLineTextField;
    @FXML
    private Pane buttonsPane;

    @FXML
    private Button downloadFilesButton;

    private static ChangeRequest currRequest;
    private ClientController clientController;

    public static ChangeRequest getCurrRequest() {
        return currRequest;
    }

    public static void setCurrRequest(ChangeRequest currRequest) {
        CrDetails.currRequest = currRequest;
    }

    public void initialize() {
        try {
            clientController = ClientController.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> params = new ArrayList<>();
        params.add(ClientController.getUser().getId());
        params.add(currRequest.getId());


        ServerService loadRequestData = new ServerService(ServerService.DatabaseService.Get_Request_Details, params);

        clientController.handleMessageFromClientUI(loadRequestData);

    }







    @FXML
    void backToHome() {
        try {
            IcmUtils.loadScene(this, IcmUtils.Scenes.Main_Window_New);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleMessageFromClientController(ServerService serverService) {
        switch (serverService.getDatabaseService()){
            case Get_Request_Details:
                System.out.println("adding details to screen");
                List<ChangeRequest> crList = serverService.getParams();
                setCurrRequest(crList.get(0));

                changeRequestIDTextField.setText(String.valueOf(currRequest.getId()));
                openingDateTextField.setText(currRequest.getDate().toString());
                initiatorTextField.setText(currRequest.getInitiator().getFirstName() + " " + currRequest.getInitiator().getLastName());
                infoSystemTextField.setText(currRequest.getInfoSystem().toString());
                currentPhaseTextField.setText(currRequest.getCurrPhaseName().toString());
                currentStateTextArea.textProperty().setValue(currRequest.getCurrState());
                reasonForChangeTextArea.textProperty().setValue(currRequest.getReasonForChange());
                requestedChangeTextField.textProperty().setValue(currRequest.getRequestedChange());
                commentsTextArea.textProperty().setValue(currRequest.getComment());
                phaseDeadLineTextField.setText(currRequest.getPhases().get(0).getDeadLine().toString());

                try {
                    initButtons();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void initButtons() throws Exception{
        Map<IEPhasePosition.PhasePosition, IEPhasePosition> iePhasePositionMap;
        iePhasePositionMap = currRequest.getPhases().get(0).getIePhasePosition();
        ChangeInitiator currUser = ClientController.getUser();
        Parent root = null;

        switch (currUser.getPosition()) {
            case ITD_MANAGER:
                root = FXMLLoader.load(getClass().getResource("itd/ITDButtons.fxml"));
                break;
            case CCC:
                root = FXMLLoader.load(getClass().getResource("ccc/CCCButtons.fxml"));
                break;
            case CHAIRMAN:
                FXMLLoader loader = new FXMLLoader();
                root = loader.load(getClass().getResource("ccc/CCCButtons.fxml").openStream());
                CCCButtons chairman = loader.getController();
                chairman.enableChairmanButtons();
                break;
            case SUPERVISOR:
                root = FXMLLoader.load(getClass().getResource("supervisor/SupervisorButtons.fxml"));
                break;
            default:
                root = FXMLLoader.load(getClass().getResource("initiator/InitiatorButtons.fxml"));
        }
        if (root != null)
            buttonsPane.getChildren().setAll(root);

        for (IEPhasePosition ie: iePhasePositionMap.values() ) {
            if (ie.getInformationEngineer().getId().equals(currUser.getId())) {
                switch (ie.getPhasePosition()) {
                    case EXECUTIVE_LEADER:
                        root = FXMLLoader.load(getClass().getResource("executiveLeader/ExecutiveLeaderButtons.fxml"));
                        break;
                    case EVALUATOR:
                        root = FXMLLoader.load(getClass().getResource("evaluator/EvaluatorButtons.fxml"));
                        break;
                    case TESTER:
                        root = FXMLLoader.load(getClass().getResource("tester/TesterButtons.fxml"));
                        break;
                    case PHASE_LEADER:
                        root = FXMLLoader.load(getClass().getResource("phaseLeader/PhaseLeaderButtons.fxml"));
                        break;

                }
            }
        }
        if (root != null)
            buttonsPane.getChildren().setAll(root);
    }
}
