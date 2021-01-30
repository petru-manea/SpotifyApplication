import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import { Paper, Typography } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    height: "calc(100vh - 52px)",
    display: "flex",
  },
  containerPapper: {
    width: " 60%",
    margin: "auto",
    padding:' 10px 30px',
    "text-align": "center",
    height: "60%",
    display: "flex",
    alignItems: "center",
    flexDirection: "column",
    justifyContent: "flex-start",
    background: theme.palette.primary.light,
  },
  heading: {
    fontSize: "24px",
  },
}));

export default function MainPage() {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <Paper elevation={9} className={classes.containerPapper}>
        <Typography className={classes.heading}>
          <h2>Genre Recognition</h2>
        </Typography>
        <Typography>
          The purpose of this application is to classify the songs to their
          respective genre using the ResNet-v2 pretrained neural network by
          analyzing the Mel Spectograms obtained by using the MFCC
          transformation on the audio files.
        </Typography>
        <br/>
        <Typography>
          The <b><i> "Find Songs Genre"</i></b> page is used to upload one or more audio files
          to the application which will be classified into their respective
          genres and also persist them to the application database.
        </Typography>
        <br/>
        <Typography>
          The<b><i> "Songs List"</i> </b>  page is used to retrieve information regarding songs
          already persisted and classified.
        </Typography>
      </Paper>
    </div>
  );
}
