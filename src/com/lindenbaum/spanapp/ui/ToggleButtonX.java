package com.lindenbaum.spanapp.ui;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.embed.swing.SwingFXUtils;

public class ToggleButtonX extends ToggleButton{
	
	
	private static BufferedImage unselectedXB ;
	private static BufferedImage selectedXB;
	
	public ToggleButtonX(String init) {
    	
    	Image unselected = SwingFXUtils.toFXImage(unselectedXB, null);
        Image selected = SwingFXUtils.toFXImage(selectedXB, null);
    	
        final ImageView iv = new ImageView(selected);
        this.getChildren().add(iv);

        iv.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                iv.setImage(unselected);
            }
        });
        iv.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent evt) {
                iv.setImage(selected);
            }
        });

        super.setGraphic(iv);
        // TODO other event handlers like mouse up

    } 


}
