package sample;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import com.jfoenix.controls.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Main extends Application
{
    private ObservableList<Label> list = FXCollections.observableArrayList();

    public Main()
    {
        var file = new File("D://Goals/");
        String ls[] = file.list();
        if(ls!=null)
        {
            for(String s:ls)
            {
                var status = false;
                File file1 = new File(file.getAbsolutePath()+"/"+s);
                if(!file1.isDirectory())
                {
                    try (var reader = new BufferedReader(new FileReader(file1))) {
                        String line = reader.readLine().trim();
                        if (line.equals(LocalDate.now().toString()))
                        {
                            while ((line = reader.readLine()) != null)
                            {
                                var label = new Label(line);
                                label.setFont(Font.font(null, FontWeight.BLACK, 15.0D));
                                list.add(label);
                            }
                        } else
                        {
                            file1.delete();
                        }

                    } catch (Exception e) { }

                }
            }
        }
    }

    @Override
    public void start(Stage stage)
    {
        var view = new JFXListView<Label>();
        view.setVerticalGap(5.0);
        view.setExpanded(true);
        view.setShowTooltip(true);
        view.setEditable(true);

        view.getItems().addAll(list);
        var textField = new JFXTextField();
        textField.setPromptText("Enter Your Tasks Here");
        textField.setLabelFloat(true);
        textField.setStyle("-fx-font-size:13px; -fx-prompt-text-fill:lightgray;");
        textField.setPadding(new Insets(30,10,10,10));

        var addBtn = new JFXButton("Add");
        addBtn.setDefaultButton(true);
        addBtn.setMaxWidth(Double.MAX_VALUE);

        addBtn.setSnapToPixel(true);
        addBtn.setTextAlignment(TextAlignment.CENTER);
        addBtn.setButtonType(JFXButton.ButtonType.RAISED);
        addBtn.setStyle("-fx-background-color:#56A5EC; -fx-ripple:red; -fx-text-fill:white; -fx-font-weight:bold;");
        addBtn.setOnAction(e -> {
            var data = textField.getText();
            textField.setText("");
            if(!data.equals(""))
                if(!view.getItems().contains(data))
                {
                    var label = new Label(data);
                    label.setFont(Font.font(null,FontWeight.BOLD,15.0D));
                    view.getItems().add(label);
                }
        });

        var removeBtn = new JFXButton("Remove");
        removeBtn.setMaxWidth(Double.MAX_VALUE);
       // removeBtn.setFont(new Font(10.0));
        removeBtn.setTextAlignment(TextAlignment.CENTER);
        removeBtn.setButtonType(JFXButton.ButtonType.RAISED);
        removeBtn.setStyle("-fx-background-color:#56A5EC; -fx-text-fill:white; -fx-font-weight:bold;");
        removeBtn.setOnAction(e -> {
            if(view.getItems().size()!=0) {
                view.getItems().removeAll(view.getSelectionModel().getSelectedItems());
            }
        });

        var removeAll = new JFXButton("Remove All");
        removeAll.setMaxWidth(Double.MAX_VALUE);
        //removeAll.setFont(new Font(10.0));
        removeAll.setButtonType(JFXButton.ButtonType.RAISED);
        removeAll.setStyle("-fx-background-color:#56A5EC; -fx-text-fill:white; -fx-font-weight:bold;");
        removeAll.setTextAlignment(TextAlignment.CENTER);
        removeAll.setOnAction(e ->{
            if(view.getItems().size()!=0)
                view.getItems().clear();
        });

        var saveBtn = new  JFXButton("Save");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
       // saveBtn.setFont(new Font(10.0));
        saveBtn.setButtonType(JFXButton.ButtonType.RAISED);
        saveBtn.setStyle("-fx-background-color:#56A5EC; -fx-text-fill:white; -fx-font-weight: bold;");
        saveBtn.setOnAction(e ->
        {
            if(view.getItems().size()!=0)
            {
                var date = LocalDate.now();
                var day = date.getDayOfWeek();
                try (var writer = new PrintWriter(new File("D://Goals/file" + day + ".txt")))
                {
                    var file = new FileOutputStream("D://Goals/Images/file"+day+".jpg");
                    writer.println(date);
                    view.getItems().forEach(label ->{
                        writer.println(label.getText());
                    });
                    ImageIO.write(this.createImage(date.toString(),view.getItems()),"jpg",file);
                } catch (IOException e1) {System.out.println(e1);}
            }
        });

        var vbox = new VBox(5.0D,addBtn,removeBtn,removeAll,saveBtn);
        vbox.setFillWidth(true);
        vbox.setPadding(new Insets(10));
        var hbox = new HBox(10.0D,view);
        hbox.setPadding(new Insets(10));
        HBox.setHgrow(view, Priority.ALWAYS);

        var root = new BorderPane();
        root.setTop(textField);
        root.setCenter(hbox);
        root.setRight(vbox);

        var scene = new Scene(root,500,500);

        stage.setScene(scene);
        stage.setTitle("Goal Setting App");
        stage.show();
    }
    private BufferedImage createImage(String date,ObservableList<Label> list)
    {
        var img = new BufferedImage(800,800,BufferedImage.TYPE_INT_RGB);
        var color = Color.BLUE;
        for(int i=0;i<img.getHeight();i++)
        {
            for(int j=0;j<img.getWidth(); j++)
            {
                img.setRGB(i,j,color.getRGB());
            }
        }

        var g = img.getGraphics();
        g.setFont(new java.awt.Font(null,java.awt.Font.BOLD,45));
        g.drawString(date,20,100);
        int x = 100,y=200;
        for(Label lb:list)
        {
            String s = lb.getText();
            g.drawString(s,x,y);
            y+=50;
        }

        return img;
    }
    public static void main(String args[]){Application.launch(args);}
}
