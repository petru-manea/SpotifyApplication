import React, { useCallback, useState } from "react";
import { makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Paper from "@material-ui/core/Paper";
import { useDropzone } from "react-dropzone";
import Accordion from "@material-ui/core/Accordion";
import AccordionDetails from "@material-ui/core/AccordionDetails";
import AccordionSummary from "@material-ui/core/AccordionSummary";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import SongGenreService from "../service/service";

const useStyles = makeStyles((theme) => ({
  root: {
    padding: "10px",
    width: "100%",
    height: "100%",
  },
  paper: {
    height: "200px",
    display: "flex",
    "align-items": "center",
    "justify-content": "center",
    "text-align": "center",
    background: theme.palette.primary.light,
  },
  accordion: {
    background: theme.palette.secondary.dark,
    "margin-top": "15px",
  },
  acordionDetails: {
    backgroundColor: theme.palette.secondary.main,
    "justify-content": "space-around",
  },
  heading: {
    fontSize: theme.typography.pxToRem(15),
    flexBasis: "33.33%",
    flexShrink: 0,
  },
  secondaryHeading: {
    fontSize: theme.typography.pxToRem(15),
    color: theme.palette.text.secondary,
    background: theme.palette.info,
  },
}));
export default function FindSongGenre() {
  const classes = useStyles();
  const [expanded, setExpanded] = useState(false);
  const [uploadedFiles, setuploadedFiles] = useState([]);
  const [uploadedFilesFailed, setuploadedFilesFailed] = useState([]);
  const handleAccordionChange = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : false);
  };

  const onDrop = useCallback(async (acceptedFiles) => {
    let fileArr = [];
    let failedFiles = [];
    for (let i = 0; i < acceptedFiles.length; i++) {
      await SongGenreService.upload(acceptedFiles[i])
        // eslint-disable-next-line no-loop-func
        .then((response) => {
          if (response.status === "OK") {
            fileArr = [...fileArr, response.data];
            console.log(response);
            setuploadedFiles(fileArr);
          } else {
            failedFiles = [...failedFiles, acceptedFiles[i]];
            console.log(response);
            setuploadedFilesFailed(failedFiles);
          }
        });
    }
  }, []);
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });
  return (
    <div className={classes.root}>
      <Paper elevation={3}>
        <div {...getRootProps()} className={classes.paper}>
          <input {...getInputProps()} />
          {isDragActive ? (
            <p>Drop the files here ...</p>
          ) : (
            <p>
              To clasify your songs and see more detail,
              <br />
              Drag&drop some files here, or click to select files
            </p>
          )}
        </div>
      </Paper>
      <div>
        {uploadedFiles.map((file) => (
          <Accordion
            expanded={expanded === file.id}
            key={file.id}
            onChange={handleAccordionChange(file.id)}
          >
            <AccordionSummary
              className={classes.accordion}
              expandIcon={<ExpandMoreIcon />}
              aria-controls={file.id}
              id={file.id}
            >
              <Typography className={classes.heading}>
                File Name: {file.filename}
              </Typography>
              <Typography className={classes.secondaryHeading}>
                Main Genre: {file.mainType.predictedType}
              </Typography>
            </AccordionSummary>

            <AccordionDetails className={classes.acordionDetails}>
              <div>
                <Typography>
                  Main Genre: {file.mainType.predictedType}
                </Typography>

                <Typography>
                  Prediction Value: {file.mainType.predictionValue}
                </Typography>
              </div>
              <div>
                <Typography>Sub Genre: {file.subType.predictedType}</Typography>

                <Typography>
                  Prediction Value: {file.subType.predictionValue}
                </Typography>
              </div>
            </AccordionDetails>
          </Accordion>
        ))}
      </div>
      <div>
        {uploadedFilesFailed.map((file) => (
          <div className={classes.heading}>
            {file.name} file couldn't be procesed!{" "}
          </div>
        ))}
      </div>
    </div>
  );
}
