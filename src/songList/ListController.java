// Junfeng Wang NetID: jw139, Joey Zheng NetID: jz813
package songList;

import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class ListController {
    @FXML
    private ListView<Song> listView;

    @FXML
    private TextField SongTitle;
    @FXML
    private TextField Artist;
    @FXML
    private TextField Album;
    @FXML
    private TextField Year;

    @FXML
    private TextField detailSongTitle;
    @FXML
    private TextField detailArtist;
    @FXML
    private TextField detailAlbum;
    @FXML
    private TextField detailYear;

    private int listSize = 0;
    File saveFile = new File("save_here.txt");

    @FXML
    public void initialize() {

        if (saveFile.exists()) {
            Song song;
            try {
                List<String> linesLoadedFromFile = Files.readAllLines(saveFile.toPath());
                for (String str : linesLoadedFromFile) {
                    String[] export = str.split(" ");
                    if (export.length == 4)
                        song = new Song(export[0], export[1], export[2], Integer.parseInt(export[3]));
                    else if (export.length == 2)
                        song = new Song(export[0], export[1]);
                    else {
                        try {
                            song = new Song(export[0], export[1], Integer.parseInt(export[2]));
                        } catch (Exception e) {
                            song = new Song(export[0], export[1], export[2]);
                        }
                    }
                    listView.getItems().add(song);

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void start() {

    }

    public void add() {
        if (SongTitle.getText().trim().equals("") || Artist.getText().trim().equals("")) {
            needSongArtistWarning();
            return;
        }
        if (SongTitle.getText().trim().contains("|") ||
                Artist.getText().trim().contains("|") ||
                Album.getText().trim().contains("|")) {
            verticalBarWarning();
            return;
        }
        try {
            if (!Year.getText().trim().isEmpty()) {
                if (Integer.parseInt(Year.getText().trim()) <= -1) {
                    yearWarning();
                    return;
                }
            }
        } catch (NumberFormatException e) {
            needNumberWarning();
            return;
        }

        for (Song exist : listView.getItems()) {
            if (exist.getSongTitle().equals(SongTitle.getText().trim()) &&
                    exist.getArtist().equals(Artist.getText().trim())) {
                songExistsWarning();
                return;
            }
        }
        Song song;
        if (confirmation()) {
            if (Album.getText().trim().equals("") && Year.getText().trim().equals(""))
                song = new Song(SongTitle.getText().trim(), Artist.getText().trim());
            else if (Album.getText().trim().equals("")) {
                try {
                    song = new Song(SongTitle.getText().trim(), Artist.getText().trim(),
                            Integer.parseInt(Year.getText().trim()));
                } catch (NumberFormatException e) {
                    needNumberWarning();
                    return;
                }
            } else if (Year.getText().trim().equals("")) {
                song = new Song(SongTitle.getText().trim(), Artist.getText().trim(), Album.getText().trim());
            } else {
                try {
                    song = new Song(SongTitle.getText().trim(), Artist.getText().trim(), Album.getText().trim(),
                            Integer.parseInt(Year.getText().trim()));
                } catch (NumberFormatException e) {
                    needNumberWarning();
                    return;
                }
            }
            listView.getItems().add(song);
            listSize++;
            listView.getSelectionModel().select(song);
            getDetails();
            SongTitle.clear();
            Artist.clear();
            Album.clear();
            Year.clear();
            sortListView();
            save();
        }
    }

    public void delete() {
        if (confirmation()) {
            int index = listView.getSelectionModel().getSelectedIndex();
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            listSize--;

            if (index == 0)
                listView.getSelectionModel().selectFirst();
            else if (listView.getSelectionModel().getSelectedIndex() == listSize - 1)
                ;
            else
                listView.getSelectionModel().selectNext();
            getDetails();
            save();
        }
    }

    public void deleteNoConfirm() {
        int index = listView.getSelectionModel().getSelectedIndex();
        listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        listSize--;

        if (index == 0)
            listView.getSelectionModel().selectFirst();
        else if (listView.getSelectionModel().getSelectedIndex() == listSize - 1)
            ;
        else
            listView.getSelectionModel().selectNext();
        getDetails();
        save();
    }

    public void edit() {
        addWhenEdit();
        save();
    }

    private void save() {
        ObservableList<Song> list = listView.getItems();
        ListView<String> stringList = new ListView<>();

        try {
            for (Object item : list) {
                String temp;

                try {
                    if (((Song) item).getYear() < 0)
                        temp = ((Song) item).getSongTitle() + " " + ((Song) item).getArtist() + " "
                                + ((Song) item).getAlbum();
                    else
                        temp = ((Song) item).getSongTitle() + " " + ((Song) item).getArtist() + " "
                                + ((Song) item).getAlbum() + " " + ((Song) item).getYear();
                } catch (Exception e) {
                    if (((Song) item).getYear() < 0)
                        temp = ((Song) item).getSongTitle() + " " + ((Song) item).getArtist();
                    else
                        temp = ((Song) item).getSongTitle() + " " + ((Song) item).getArtist();
                }
                stringList.getItems().add(temp);
            }
            Files.write(saveFile.toPath(), stringList.getItems().subList(0, stringList.getItems().size()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean confirmation() {
        Boolean confirm = false;

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm your action");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
            confirm = true;
        return confirm;

    }

    private void sortListView() {
        ObservableList<Song> list = listView.getItems();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                int comp = list.get(i).getSongTitle().compareTo(list.get(j).getSongTitle());
                if (comp > 0) { // if it's 1, then they are not same, and get placed into list
                    Song temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                } else if (comp == 0) { // same song title
                    if (list.get(i).getArtist().compareTo(list.get(j).getArtist()) > 0) {
                        Song temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }
        listView.setItems(list);
    }

    private void songExistsWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("The song already exist in the libary");
        alert.showAndWait();
    }

    private void needNumberWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Year needs to be a number!");
        alert.showAndWait();
    }

    private void needSongArtistWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("At least Song name and Artist is required");
        alert.showAndWait();
    }

    private void yearWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Year must be a postive integer");
        alert.showAndWait();
    }

    private void verticalBarWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("The '|' vertical bar character is not allowed");
        alert.showAndWait();
    }

    public void getDetails() {
        detailYear.clear();
        detailAlbum.clear();

        if (listView.getSelectionModel().getSelectedItem() == null) {
            detailArtist.clear();
            detailSongTitle.clear();
            return;
        }
        try {
            detailSongTitle.setText(listView.getSelectionModel().getSelectedItem().getSongTitle());
            detailArtist.setText(listView.getSelectionModel().getSelectedItem().getArtist());
            detailAlbum.setText(listView.getSelectionModel().getSelectedItem().getAlbum());
            if (listView.getSelectionModel().getSelectedItem().getYear() != -1)
                detailYear.setText(listView.getSelectionModel().getSelectedItem().getYear() + "");

        } catch (Exception e) {
            try {
                detailSongTitle.setText(listView.getSelectionModel().getSelectedItem().getSongTitle());
                detailArtist.setText(listView.getSelectionModel().getSelectedItem().getArtist());
                if (listView.getSelectionModel().getSelectedItem().getYear() != -1)
                    detailYear.setText(listView.getSelectionModel().getSelectedItem().getYear() + "");
            } catch (Exception r) {
                return;
            }
            return;
        }

    }

    private void addWhenEdit() {
        if (detailSongTitle.getText().trim().equals("") || detailArtist.getText().trim().equals("")) {
            needSongArtistWarning();
            return;
        }
        if (detailSongTitle.getText().trim().contains("|") ||
                detailArtist.getText().trim().contains("|") ||
                detailAlbum.getText().trim().contains("|")) {
            verticalBarWarning();
            return;
        }
        try {
            if (!detailYear.getText().trim().isEmpty()) {
                if (Integer.parseInt(detailYear.getText().trim()) <= -1) {
                    yearWarning();
                    return;
                }
            }
        } catch (NumberFormatException e) {
            needNumberWarning();
            return;
        }
        Song temp = listView.getSelectionModel().getSelectedItem();
        for (Song exist : listView.getItems()) {
            if (exist == temp)
                continue;
            if (exist.getSongTitle().equals(detailSongTitle.getText().trim()) &&
                    exist.getArtist().equals(detailArtist.getText().trim())) {
                songExistsWarning();
                getDetails();
                return;
            }
        }

        Song song;

        if (confirmation()) {
            if (detailAlbum.getText().trim().equals("") && detailYear.getText().trim().equals("")) {
                song = new Song(detailSongTitle.getText().trim(), detailArtist.getText().trim());
            } else if (detailAlbum.getText().trim().equals("")) {
                try {
                    song = new Song(detailSongTitle.getText().trim(), detailArtist.getText().trim(),
                            Integer.parseInt(detailYear.getText().trim()));
                } catch (NumberFormatException e) {
                    needNumberWarning();
                    return;
                }
            } else if (detailYear.getText().trim().equals("")) {
                song = new Song(detailSongTitle.getText().trim(), detailArtist.getText().trim(),
                        detailAlbum.getText().trim());
            } else {
                try {
                    song = new Song(detailSongTitle.getText().trim(), detailArtist.getText().trim(),
                            detailAlbum.getText().trim(),
                            Integer.parseInt(detailYear.getText().trim()));
                } catch (NumberFormatException e) {
                    needNumberWarning();
                    return;
                }
            }
            deleteNoConfirm();
            listView.getItems().add(song);
            sortListView();
            listView.getSelectionModel().select(song);
            getDetails();
        }
    }

}