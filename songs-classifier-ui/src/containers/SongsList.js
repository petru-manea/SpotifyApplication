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
  Chip,
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
    textAlign:'center'
  },
  progressWrapper: {
    width: "50%",
    margin: "auto",
    overflow: "hidden",
    "text-align": "center",
    "margin-top": "10%",
  },
  musicNote: {
    color: "#fff",
    backgroundColor: "#637da2",
  },
  containerPapper: {
    width: "50%",
    height: " 200px",
    margin: "auto",
    display: "flex",
    "text-align": "center",
    "justify-content": "center",
    "flex-direction": "column",
    "margin-top": "10%",
    background: "darkred",
  },
  chipWrapper:{
    color: "#fff",
    backgroundColor: "rgb(73, 83, 103)",
    margin: '10px',
    'font-size': '14px',
    height: '32px',
    width:"106px",
    cursor:"pointer"
  },
  chipWrapperActive:{
    backgroundColor: "#637da2",
    color: "#fff",
    margin: '10px',
    'font-size': '14px',
    height: '32px',
    width:"106px"

  },
}));

export default function SongList() {
  const classes = useStyles();
  const [songsList, setSongsList] = useState([]);
  const [isSongsList, setIsSongsList] = useState(false);
  const [somethingWentWrong, setsomethingWentWrong] = useState(false);
  const [selectedGenra, setSelectedGenra] = useState('All');
  const [isLoading, setIsLoading] = useState(true);

  const genres = [
    "All",
    "Blues",
    "Classical",
    "Country",
    "Disco",
    "HipHop",
    "Jazz",
    "Metal",
    "Pop",
    "Reggae",
    "Rock",
  ];
  useEffect(() => {
    if (!isSongsList) {
      getSongList(selectedGenra);
    }
  });

  const getSongList = (id) => {
    setSelectedGenra(id)
    setSongsList([]);
    setIsSongsList(true);
    setIsLoading(true);
    setsomethingWentWrong(false);

    SongGenreService.getSongs(id).then((resp) => {
      if (resp && resp.data) {
        const data = [...new Map( resp.data.map(item =>
          [item.filename, item])).values()].map((song) => ({
          ...song,
          filename: song.filename.replace(".au", ""),
        }));
        setSongsList(data);
        setIsSongsList(true);
      setTimeout(() => {
        setIsLoading(false)

      }, 800);

      } else {
        setsomethingWentWrong(true);
        setIsSongsList(false);
        setTimeout(() => {
          setIsLoading(false)
  
        }, 1000);
      }
    });
  };
  

  return (
    <div className={classes.root}>
      <div className={classes.listContainer}>
        {genres.map((item, index) => (
          <Chip color="secondary" className={item===selectedGenra ? classes.chipWrapperActive: classes.chipWrapper} label={item} onClick={()=>getSongList(item)} key={"item"+item+index} />
        ))}
        {!somethingWentWrong ? (
          <List component="nav" aria-label="songs">
            {!isLoading ? (
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
            <h2>Something went wrong!</h2>
            <br />
            <h2>Please try again.</h2>
          </Paper>
        )}
      </div>
      <div></div>
    </div>
  );
}
