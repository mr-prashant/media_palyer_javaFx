package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable {
    @FXML private MediaView mv;
    private MediaPlayer mp;
    private Media media;
    @FXML  private Slider volumeSlider;
	private String filepath;
	@FXML Slider seekSlider;
	
	@FXML Button silent;
	 boolean mute=false;
	
	
	public void fileChooser(ActionEvent event)
	{
		FileChooser fc=new FileChooser();
		FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("File choose (*.mp4)", "*.mp4");
		fc.getExtensionFilters().add(filter);
		File file=fc.showOpenDialog(null);
		filepath=file.toURI().toString();
		if(filepath!=null) 
		{
		//	mp.stop();
		    media=new Media(filepath);
			mp=new MediaPlayer(media);
			mv.setMediaPlayer(mp);
			mp.play();
			
			// This will help to preserve the ratio of the video.
			DoubleProperty width=mv.fitWidthProperty();
			DoubleProperty height=mv.fitHeightProperty();
			
			width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));
			
			volumeSlider.setValue(mp.getVolume()*100);
			volumeSlider.valueProperty().addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable observable) {
				    mp.setVolume(volumeSlider.getValue()/100);
					
				}
			});
			

			int w=mp.getMedia().getWidth();
			int h=mp.getMedia().getHeight();
			
			double time=media.getDuration().toSeconds();
			
			seekSlider.setTranslateY(h-20);
			
			
			seekSlider.setMinWidth(w);
			seekSlider.setMinHeight(1);
			
			//seekSlider.setMaxWidth(time);
			/*seekSlider.setMin(0);
			seekSlider.setMax(time);*/
			
			//What it is doing
			seekSlider.maxProperty().bind(Bindings.createDoubleBinding(
				    () -> mp.getTotalDuration().toSeconds(),
				    mp.totalDurationProperty()));
			
			
	        
			mp.currentTimeProperty().addListener(new ChangeListener<Duration>() {

				@Override
				public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
						Duration newValue) {
					seekSlider.setValue(newValue.toSeconds());
					
				}
			});
			
			seekSlider.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					mp.seek(Duration.seconds(seekSlider.getValue()));
					
				}
			});
		
			
			mp.play();
		}
		
	}
	public void play(ActionEvent event)
	{
		mp.play();
		mp.setRate(1);
	}
	
	public void pause(ActionEvent event)
	{
		mp.pause();
	}
	public void fast(ActionEvent event)
	{
		mp.setRate(1.5);
	}
	public void slow(ActionEvent event)
	{
		mp.setRate(.75);
	}
	public void stop(ActionEvent event)
	{
	    mp.stop();
	}
	

    public void mute()
    {  
    	
    	if (mute==false) 
    	{
    		silent.setOnAction(new EventHandler<ActionEvent>() {

    			@Override
    			public void handle(ActionEvent event) {
    				
    				if(!mute) {
    					mp.setMute(true);
    					mute=true;
    				} else {
    					mp.setMute(false);
    					mute = false;
    				}
    				
    			}
    		});
        	
		}
    	
    
    }
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

	}
	
	
	
	
	
}
