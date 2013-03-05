/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import graphview.BaseShape;
import graphview.LineShape;
import graphview.GraphScene;
import graphview.BoxShape;
import com.javadocking.dock.LineDock;
import com.javadocking.dock.Position;
import com.javadocking.dockable.DefaultDockable;
import com.javadocking.dockable.Dockable;
import com.javadocking.dockable.DockableState;
import com.javadocking.dockable.DockingMode;
import com.javadocking.dockable.action.DefaultDockableStateAction;
import geometry.Vec2;
import graphview.EllipseShape;
import graphview.GraphMain;
import graphview.TextShape;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Kirill
 */
public class MainPanel extends DockablePanel{
    
    private Action closeAction;
    private Action restoreAction;
    
    GraphMain graphMain;
    Dockable[] buttonDockables;
    
    StructurePanel structure;
    ObjectPropertiesPanel objectProperties;
    OverviewPanel overview;
    
    Dockable sceneDock;
    Dockable structureDock;
    Dockable objectPropertiesDock;
    Dockable overviewDock;
     
    public MainPanel(JFrame frame,GraphMain graphMain_)
    {
        super(frame);
        graphMain=graphMain_;
        initUI();
        initScene();
    }
    
    public void initUI()
    {
        
        structure=new StructurePanel();
        structureDock = new DefaultDockable("Structure", structure, "Structure", null, DockingMode.ALL - DockingMode.FLOAT);
        structureDock=addActions(structureDock);
        centerTabbedDock.addDockable(structureDock, new Position(1));
        
        sceneDock = new DefaultDockable("Scene", graphMain.getGraphScene(), "Scene", null, DockingMode.ALL - DockingMode.FLOAT);
        sceneDock=addActions(sceneDock);
        centerTabbedDock.addDockable(sceneDock, new Position(0));
        
        JTextArea textPanel1 = new JTextArea();
        Dockable textPanelDock = new DefaultDockable("Text", textPanel1, "Text", null, DockingMode.ALL - DockingMode.FLOAT);
        textPanelDock=addActions(textPanelDock);
        botLeftTabbedDock.addDockable(textPanelDock, new Position(1));
        
        objectProperties=new ObjectPropertiesPanel();
        objectPropertiesDock = new DefaultDockable("Object Properties", objectProperties, "Object Properties", null, DockingMode.ALL - DockingMode.FLOAT);
        objectPropertiesDock=addActions(objectPropertiesDock);
        rightTabbedDock.addDockable(objectPropertiesDock, new Position(1));
        
        overview=new OverviewPanel();
        overviewDock = new DefaultDockable("Overview", overview, "Overview", null, DockingMode.ALL - DockingMode.FLOAT);
        overviewDock=addActions(overviewDock);
        topLeftTabbedDock.addDockable(overviewDock, new Position(1));
        
        
        
        buttonDockables = new Dockable[6];
        buttonDockables[0]  = createButtonDockable("ButtonDockableAdd",              "Add",               new ImageIcon("res/icons/add.png"),               "Add!");
	buttonDockables[1]  = createButtonDockable("ButtonDockableAccept",           "Accept",            new ImageIcon("res/icons/accept.png"),            "Accept!");
	buttonDockables[2]  = createButtonDockable("ButtonDockableCancel",           "Cancel",            new ImageIcon("res/icons/cancel.png"),            "Cancel!");
        buttonDockables[3]  = createButtonDockable("ButtonDockableAdd",              "Add",               new ImageIcon("res/icons/add.png"),               "Add!");
	buttonDockables[4]  = createButtonDockable("ButtonDockableAccept",           "Accept",            new ImageIcon("res/icons/accept.png"),            "Accept!");
	buttonDockables[5]  = createButtonDockable("ButtonDockableCancel",           "Cancel",            new ImageIcon("res/icons/cancel.png"),            "Cancel!");
        
        LineDock toolBarDock1 = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        toolBarDock1.addDockable(buttonDockables[0], new Position(0));
	toolBarDock1.addDockable(buttonDockables[1], new Position(1));
	toolBarDock1.addDockable(buttonDockables[2], new Position(2));

        LineDock mainToolBar = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
        mainToolBar.addDockable(buttonDockables[3], new Position(0));
        
        LineDock nodeToolBar = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
	nodeToolBar.addDockable(buttonDockables[4], new Position(1));
	        
        LineDock edgeToolBar = new LineDock(LineDock.ORIENTATION_HORIZONTAL, false, DockingMode.HORIZONTAL_TOOLBAR, DockingMode.VERTICAL_TOOLBAR);
      	edgeToolBar.addDockable(buttonDockables[5], new Position(2));
        
        compositeToolBarDockTop.addChildDock(toolBarDock1, new Position(0));
        compositeToolBarDockTop.addChildDock(mainToolBar, new Position(1));
        compositeToolBarDockTop.addChildDock(nodeToolBar, new Position(2));
        compositeToolBarDockTop.addChildDock(edgeToolBar, new Position(3));
        
    };
    
    public void initScene()
    {
        BaseShape shape=new BoxShape(10,10,200,300);
        shape.color=Color.yellow;
        
        BaseShape shape3=new BoxShape(15,15,100,100);
        shape3.color=Color.red;
        //shape.addChild(shape3);
        
        BaseShape shape2=new BoxShape(0,0,100,100);
        shape2.color=Color.red;
        shape2.setPosition(new Vec2(300,10));
        
        BaseShape dot=new EllipseShape(0,0,5,5);
        dot.move(new Vec2(200,-100));
        dot.color=Color.black;
        
        TextShape text=new TextShape("12345\n67890");
        shape.addChild(text);
        shape.setContainerMode(BaseShape.CONTAIN_NODE_TO_CHILDS);
        //shape.setContainerMode(BaseShape.CONTAIN_CHILDS_TO_NODE);
        
        BaseShape ellipse=new EllipseShape(new Vec2(0,0),100);
        ellipse.setPosition(new Vec2(40,100));
        ellipse.color=Color.CYAN;
        TextShape text2=new TextShape("QWERty");
        ellipse.addChild(text2);
        ellipse.setContainerMode(BaseShape.CONTAIN_NODE_TO_CHILDS);
        
        LineShape line = new LineShape(shape,shape2);
        line.insertPoint(dot,0);
        
        LineShape line2 = new LineShape(shape,ellipse);
        
        graphMain.getGraphScene().add(shape);
        graphMain.getGraphScene().add(shape2);
        graphMain.getGraphScene().add(line);
        //graphMain.getGraphScene().add(ellipse);
        //graphMain.getGraphScene().add(line2);
        //graphMain.getGraphScene().add(text);
    };
    
    public void hideDock(String str)
    {
        
        
        switch (str) {
            case "Scene":
                closeAction = new DefaultDockableStateAction(sceneDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(sceneDock, DockableState.NORMAL);
                if (graphMain.getGraphScene().isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
            case "ObjectProperties":
                closeAction = new DefaultDockableStateAction(objectPropertiesDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(objectPropertiesDock, DockableState.NORMAL);
                if (objectProperties.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
            case "Structure":
                closeAction = new DefaultDockableStateAction(structureDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(structureDock, DockableState.NORMAL);
                if (structure.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
            case "Overview":
                closeAction = new DefaultDockableStateAction(overviewDock, DockableState.CLOSED);
                restoreAction = new DefaultDockableStateAction(overviewDock, DockableState.NORMAL);
                if (overview.isDisplayable())
                {
                    // Close the dockable.
                    closeAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Close"));
                } 
                else 
                {
                    // Restore the dockable.
                    restoreAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Restore"));
                }
                break;
                
        }
        
       
        
    }
}
