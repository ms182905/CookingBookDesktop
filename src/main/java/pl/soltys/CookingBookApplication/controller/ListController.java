/* (C)2023 */
package pl.soltys.CookingBookApplication.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import org.springframework.stereotype.Component;

import pl.soltys.CookingBookApplication.model.Recipe;

@Component
public abstract class ListController {
  @FXML public TableView<Recipe> mainTableView = new TableView<>();
  @FXML public TableColumn<Recipe, ImageView> pictureTableColumn = new TableColumn<>("Picture");
  @FXML public TableColumn<Recipe, String> nameTableColumn = new TableColumn<>("Name");

  @FXML
  public TableColumn<Recipe, String> descriptionTableColumn = new TableColumn<>("Description");

  @FXML public ProgressIndicator progressIndicator;

  public void setColumnForTableView(TableView<Recipe> tableView) {
    pictureTableColumn.setCellValueFactory((new PropertyValueFactory<>("Picture")));
    nameTableColumn.setCellValueFactory((new PropertyValueFactory<>("Name")));
    descriptionTableColumn.setCellValueFactory((new PropertyValueFactory<>("Description")));
    tableView.setFixedCellSize(200);
  }

  public void wrapTextForTableColumn(TableColumn<Recipe, String> tableColumn) {
    tableColumn.setCellFactory(
        tc -> {
          TableCell<Recipe, String> cell = new TableCell<>();
          Text text = new Text();
          cell.setGraphic(text);
          cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
          text.wrappingWidthProperty().bind(tableColumn.widthProperty());
          text.textProperty().bind(cell.itemProperty());
          return cell;
        });
  }
}
