package entities;

import java.io.File;
import java.time.LocalDate;

public class ChangeRequest {

	private String id;
	private InfoSystem infoSystem;
	private String currState;
	private String requestedChange;
	private String reasonForChange;
	private String comment;
	private File[] files;
	private LocalDate date;
	private Phase[] phases;
	private Phase.PhaseName currPhase;
	private boolean suspended;

	public String getId() {
		return this.id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public InfoSystem getInfoSystem() {
		return this.infoSystem;
	}

	/**
	 * 
	 * @param infoSystem
	 */
	public void setInfoSystem(InfoSystem infoSystem) {
		this.infoSystem = infoSystem;
	}

	public String getCurrState() {
		return this.currState;
	}

	/**
	 * 
	 * @param currState
	 */
	public void setCurrState(String currState) {
		this.currState = currState;
	}

	public String getRequestedChange() {
		return this.requestedChange;
	}

	/**
	 * 
	 * @param requestedChange
	 */
	public void setRequestedChange(String requestedChange) {
		this.requestedChange = requestedChange;
	}

	public String getReasonForChange() {
		return this.reasonForChange;
	}

	/**
	 * 
	 * @param reasonForChange
	 */
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getComment() {
		return this.comment;
	}

	/**
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public File[] getFiles() {
		return this.files;
	}

	/**
	 * 
	 * @param files
	 */
	public void setFiles(File[] files) {
		this.files = files;
	}

	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * 
	 * @param date
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Phase[] getPhases() {
		return this.phases;
	}

	/**
	 * 
	 * @param phases
	 */
	public void setPhases(Phase[] phases) {
		this.phases = phases;
	}

	public Phase.PhaseName getCurrPhase() {
		return this.currPhase;
	}

	/**
	 * 
	 * @param currPhase
	 */
	public void setCurrPhase(Phase.PhaseName currPhase) {
		this.currPhase = currPhase;
	}

	public boolean getSuspended() {
		return this.suspended;
	}

	/**
	 * 
	 * @param suspended
	 */
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

}