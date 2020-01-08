package client.mainWindow.newRequest;

import client.ClientController;
import client.ClientUI;
import common.IcmUtils;
import entities.ChangeRequest;
import entities.InfoSystem;
import entities.Phase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import server.ServerService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewRequest implements ClientUI {

    ClientController clientController;
    @FXML
    private ChoiceBox<InfoSystem> infoSystemChoiceBox;
    @FXML
    private TextArea currentStateTextArea;
    @FXML
    private TextArea requestedChangeTextArea;
    @FXML
    private TextArea reasonForChangeTextArea;
    @FXML
    private TextArea commentsTextArea;
    @FXML
    private Button attachFileButton;
    @FXML
    private Button submitNewChangeRequestButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ListView<File> filesListView;
    private File[] filesToAttach;
    public ChoiceBox<InfoSystem> getInfoSystemChoiceBox() {
        return infoSystemChoiceBox;
    }

    public TextArea getCurrentStateTextArea() {
        return currentStateTextArea;
    }

    public TextArea getRequestedChangeTextArea() {
        return requestedChangeTextArea;
    }

    public TextArea getReasonForChangeTextArea() {
        return reasonForChangeTextArea;
    }

    public void initialize() {
        try {
            clientController = ClientController.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        infoSystemChoiceBox.setItems(InfoSystem.getAll());

        // disable Create button any field is invalid
        BooleanBinding bb = Bindings.createBooleanBinding(() -> {
                    InfoSystem infoSystem = infoSystemChoiceBox.getSelectionModel().getSelectedItem();
                    String currState = currentStateTextArea.textProperty().getValue();
                    String requestedChange = requestedChangeTextArea.textProperty().getValue();
                    String reasonForChange = reasonForChangeTextArea.textProperty().getValue();

                    // disable, if one selection is missing or from is not smaller than to
                    return (infoSystem == null || currState.trim().equals("") || requestedChange.trim().equals("") || reasonForChange.trim().equals(""));
                }, infoSystemChoiceBox.valueProperty(),
                currentStateTextArea.textProperty(),
                requestedChangeTextArea.textProperty(),
                reasonForChangeTextArea.textProperty()
        );
        submitNewChangeRequestButton.disableProperty().bind(bb);


    }


    @FXML
    void addFiles(ActionEvent event) {
    	
    	
    	FileChooser fileCh=new FileChooser();
    	List<File> files=fileCh.showOpenMultipleDialog(client.ClientMain.getPrimaryStage());
    	filesToAttach=new File[files.size()];
		ObservableList<File> listTemp = FXCollections.observableArrayList();
		int i=0;
		for (File f : files) {
			listTemp.add(f);
			filesToAttach[i]=f;
			i++;
		}
		filesListView.setItems(listTemp);// show the files on the screen
    }

    @FXML
    public void submitNewChangeRequest() {
        System.out.println("new request");

        ChangeRequest newRequest = new ChangeRequest();
        newRequest.setInitiator(ClientController.getUser());
        newRequest.setDate(LocalDate.now());
        newRequest.setInfoSystem(infoSystemChoiceBox.getSelectionModel().getSelectedItem());
        newRequest.setCurrState(currentStateTextArea.textProperty().getValue());
        newRequest.setRequestedChange(requestedChangeTextArea.textProperty().getValue());
        newRequest.setReasonForChange(reasonForChangeTextArea.textProperty().getValue());
        newRequest.setComment(commentsTextArea.textProperty().getValue());
        newRequest.setCurrPhaseName(Phase.PhaseName.SUBMITTED);
        newRequest.setFiles(filesToAttach);
        // TODO: add implementation to files

        Phase evaluation = new Phase();
        evaluation.setName(newRequest.getCurrPhaseName());
        evaluation.setPhaseStatus(Phase.PhaseStatus.SUBMITTED);

        List<Phase> phaseList = new ArrayList<>();
        phaseList.add(evaluation);
        newRequest.setPhases(phaseList);

        List<ChangeRequest> changeRequestList = new ArrayList<>();
        changeRequestList.add(newRequest);
        ServerService addNewRequest = new ServerService(ServerService.DatabaseService.Add_New_Request, changeRequestList);

        clientController.handleMessageFromClientUI(addNewRequest);
    }

    @Override
    public void handleMessageFromClientController(ServerService serverService) {
        switch (serverService.getDatabaseService()) {
            case Add_New_Request:
                IcmUtils.displayInformationMsg("Request Submitted Successfully", "Congratulations!",  "Your request has been submitted successfully.\nYou will get updates to your email.");
                try {
                    IcmUtils.getPopUp().close();
                    IcmUtils.loadScene(this, IcmUtils.Scenes.Main_Window_New);
                } catch (IOException e) {
                    e.printStackTrace();
                    IcmUtils.displayErrorMsg(e.getMessage());
                }
                break;
            case Error:
                IOException ioException = ((IOException) serverService.getParams().get(0));
                IcmUtils.displayErrorMsg("Server Error", "Server Error", ioException.getMessage());
                break;
        }
    }

    @FXML
    private void backToMainWindow() {
        IcmUtils.getPopUp().close();
    }
}

