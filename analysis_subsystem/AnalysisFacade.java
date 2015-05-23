package analysis_subsystem;

import analysis_subsystem.auxillary.areas_analysis.AnalysisConclusion;
import analysis_subsystem.auxillary.capture_regions_management.RegionSettingManager;
import analysis_subsystem.auxillary.capture_regions_management.VideoFlowDecorator;
import analysis_subsystem.auxillary.areas_analysis.AnalysisPerformer;
import analysis_subsystem.exceptions.AnalysisException;
import analysis_subsystem.gui.FrameAnalysisPanel;
import analysis_subsystem.interfaces.AnalysisPerformingProcessable;
import analysis_subsystem.interfaces.AnalysisSubsystemCommonInterface;
import analysis_subsystem.interfaces.CaptureRegionsViewable;
import capture_subsystem.interfaces.ImagePanelActionListenable;
import capture_subsystem.interfaces.VideoFlowDecorable;
import core.auxillary.ShapeDrawers.ShapeDrawer;

import javax.swing.*;

public class AnalysisFacade implements AnalysisSubsystemCommonInterface, AnalysisPerformingProcessable {

    JPanel componentGUI;
    RegionSettingManager regionSettingManager;
    AnalysisPerformer analysisPerformer;
    ShapeDrawer drawer;
    VideoFlowDecorator videoFlowDecorator;
    Thread analysisThread;

    public AnalysisFacade(ShapeDrawer drawer) {
        componentGUI = new FrameAnalysisPanel();
        this.drawer = drawer;
    }

    @Override
    public JPanel getGUIPanel() {
        return componentGUI;
    }

    @Override
    public void performInstantAnalysis() {
        analysisPerformer = AnalysisPerformer.getInstance(regionSettingManager.getMeniscusInf(), regionSettingManager.getDeviationInf(),
                regionSettingManager.getShaperInf(), this);
        analysisThread = new Thread(analysisPerformer,"analysis thread");
        regionSettingManager.addCaptureCoordEditable(analysisPerformer);
        analysisThread.start();
    }

    @Override
    public void performIterativeAnalysis() {
        analysisPerformer = AnalysisPerformer.getInstance(regionSettingManager.getMeniscusInf(), regionSettingManager.getDeviationInf(),
                regionSettingManager.getShaperInf(), this);
        analysisThread = new Thread(analysisPerformer,"analysis thread");
        regionSettingManager.addCaptureCoordEditable(analysisPerformer);
        analysisPerformer.permitAnalysis();
        analysisThread.start();
    }

    @Override
    public void performAnalysis() {
        System.out.println("analysis");
    }

    @Override
    public void stopAnalysis() {
        analysisPerformer.forbidAnalysis();
    }

    @Override
    public void setActionListenable(ImagePanelActionListenable actionListenable, CaptureRegionsViewable regionsViewable) {
        regionSettingManager = new RegionSettingManager(actionListenable, regionsViewable);
        regionSettingManager.addCaptureCoordEditable(videoFlowDecorator);
    }

    public void setDecorable(VideoFlowDecorable decorable) {
        videoFlowDecorator = new VideoFlowDecorator(decorable, drawer);
    }

    @Override
    public ShapeDrawer getShapeDrawer() {
        return drawer;
    }

    @Override
    public void processException(AnalysisException e) {
        JOptionPane.showMessageDialog(null,e.getMessage(), "Analysis exception",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void processConclusion(AnalysisConclusion conclusion) {
        System.out.println(conclusion.toString());
    }
}
