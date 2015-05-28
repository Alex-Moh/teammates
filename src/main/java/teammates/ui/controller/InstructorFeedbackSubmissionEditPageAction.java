package teammates.ui.controller;

import teammates.common.datatransfer.FeedbackSessionAttributes;
import teammates.common.datatransfer.FeedbackSessionQuestionsBundle;
import teammates.common.datatransfer.InstructorAttributes;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.util.Const;
import teammates.logic.api.GateKeeper;

public class InstructorFeedbackSubmissionEditPageAction extends FeedbackSubmissionEditPageAction {

    @Override
    protected boolean isSpecificUserJoinedCourse() {
        // Instructor is always already joined
        return true;
    }

    @Override
    protected void verifyAccesibleForSpecificUser() {
        InstructorAttributes instructor = logic.getInstructorForGoogleId(courseId, account.googleId);
        FeedbackSessionAttributes session = logic.getFeedbackSession(feedbackSessionName, courseId);
        boolean creatorOnly = false;
        new GateKeeper().verifyAccessible(instructor, session, creatorOnly, 
                                          Const.ParamsNames.INSTRUCTOR_PERMISSION_SUBMIT_SESSION_IN_SECTIONS);
    }

    @Override
    protected String getUserEmailForCourse() {
        return logic.getInstructorForGoogleId(courseId, account.googleId).email;
    }

    @Override
    protected FeedbackSessionQuestionsBundle getDataBundle(String userEmailForCourse) throws EntityDoesNotExistException {
        return logic.getFeedbackSessionQuestionsBundleForInstructor(
                             feedbackSessionName, courseId, userEmailForCourse);
    }
    
    @Override
    protected boolean isSessionOpenForSpecificUser(FeedbackSessionAttributes session) {
        return session.isOpened() || session.isPrivateSession();
    }

    @Override
    protected void setStatusToAdmin() {
        statusToAdmin = "Show instructor feedback submission edit page<br>"
                        + "Session Name: " + feedbackSessionName + "<br>"
                        + "Course ID: " + courseId;
    }

    @Override
    protected ShowPageResult createSpecificShowPageResult() {
        return createShowPageResult(Const.ViewURIs.INSTRUCTOR_FEEDBACK_SUBMISSION_EDIT, data);
    }

    @Override
    protected RedirectResult createSpecificRedirectResult() {
        return createRedirectResult(Const.ActionURIs.INSTRUCTOR_HOME_PAGE);
    }
}
