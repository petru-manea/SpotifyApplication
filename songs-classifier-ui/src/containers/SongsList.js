import React, { useEffect, useState } from "react";
import { makeStyles } from "@material-ui/core/styles";
import SongGenreService from "../service/service";
import {
  Avatar,
  CircularProgress,
  Divider,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  Paper,
} from "@material-ui/core";
import { MusicNote } from "@material-ui/icons";

const useStyles = makeStyles((theme) => ({
  root: {
    display: "flex",
    justifyContent: "space-between",
    overflow: "auto",
    "max-height": "calc(100vh - 54px)",
    margin: "auto",
  },
  listContainer: {
    "min-width": "99vw",
    margin: "auto",
  },
  progressWrapper: {
    width: "50%",
    margin: "auto",
    overflow: "hidden",
    "text-align": "center",
    "margin-top": "20%",
  },
  musicNote: {
    color: "#fff",
    backgroundColor: "#637da2",
  },
  containerPapper:{
    width: '50%',
    height:' 200px',
    margin: 'auto',
    display: 'flex',
    'text-align': 'center',
    'justify-content': 'center',
    'flex-direction': 'column',
    'margin-top': '10%',
    'background': 'darkred',
  }
}));

export default function SongList() {
  const classes = useStyles();
  const [songsList, setSongsList] = useState([]);
  const [isSongsList, setIsSongsList] = useState(false);
  const [somethingWentWrong, setsomethingWentWrong] = useState(false);

  useEffect(() => {
    if (!isSongsList) {
      getSongList();
    }
  });

  const getSongList = () => {
    SongGenreService.getSongs().then((resp) => {
      if (resp && resp.data) {
        const data = resp.data.map((song) => ({
          ...song,
          filename: song.filename.replace(".au", ""),
        }));
        setSongsList(data);
        setIsSongsList(true);
        setsomethingWentWrong(false);
      } else {
        setsomethingWentWrong(true);
      }
    });
  };

  return (
    <div className={classes.root}>
      <div className={classes.listContainer}>
        {!somethingWentWrong ? (
          <List component="nav" aria-label="songs">
            {isSongsList ? (
              songsList.map((song, index) => (
                <div key={song.id}>
                  <ListItem button>
                    <ListItemAvatar>
                      <Avatar className={classes.musicNote}>
                        <MusicNote />
                      </Avatar>
                    </ListItemAvatar>
                    <ListItemText primary={song.filename} />
                    <ListItemText
                      primary={`${song.mainType.predictedType}: ${(
                        song.mainType.predictionValue * 100
                      ).toFixed(2)}% `}
                    />
                    <ListItemText
                      primary={`${song.subType.predictedType}: ${(
                        song.subType.predictionValue * 100
                      ).toFixed(2)}%`}
                    />
                  </ListItem>
                  {index < songsList.length && <Divider />}
                </div>
              ))
            ) : (
              <div className={classes.progressWrapper}>
                <CircularProgress size="200px" />
              </div>
            )}
          </List>
        ) : (
          <Paper elevation={9} className={classes.containerPapper}>
          <h2>
          Something went wrong! 
          </h2>
          <br/>
          <h2>
          Please try again.
          </h2>
          </Paper>
        )}
      </div>
      <div></div>
    </div>
  );
}
