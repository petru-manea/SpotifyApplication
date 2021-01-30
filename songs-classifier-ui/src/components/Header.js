import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Typography from "@material-ui/core/Typography";
import FindSongGenre from "../containers/FindSongGenre";
import SongList from "../containers/SongsList";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  NavLink,
  Redirect,
} from "react-router-dom";
import MainPage from "../containers/MainPage";

const useStyles = makeStyles((theme) => ({
  root: {
    flexDirection: "row",
    justifyContent: "space-between",
    padding: "10px",
    alignItems: "center",
  },
  title: {},
  spacer: { flex: 1 },
  items: {},
  item: {
    color: theme.palette.text.secondary,
    "text-decoration": "none",
    padding: "10px 15px",
    "font-size": "16px",
  },
  selected: {
    color: theme.palette.text.primary,
  },
}));

export default function HeaderTabs() {
  const classes = useStyles();
  return (
    <Router>
      <AppBar className={classes.root} position="static">
        <Typography className={classes.title} variant="h6" noWrap>
          Genre Recognition
        </Typography>
        <div className={classes.spacer}></div>
        <div className={classes.items}>
          <NavLink
            activeStyle={{
              fontWeight: "bold",
              color: "white",
            }}
            className={classes.item}
            to="/home"
          >
            Home
          </NavLink>
          <NavLink
            className={classes.item}
            activeStyle={{
              fontWeight: "bold",
              color: "white",
            }}
            to={`/find-song-genre`}
          >
            Find Songs Genre
          </NavLink>
          <NavLink
            activeStyle={{
              fontWeight: "bold",
              color: "white",
            }}
            className={classes.item}
            to="/songsList"
          >
            Songs List
          </NavLink>
        </div>
      </AppBar>
      <Switch>
     <Route exact path="/">
  <Redirect to="/home" /> : <MainPage />
</Route>

        <Route path="/home" exact component={MainPage} />
        <Route path="/find-song-genre" component={FindSongGenre} />
        <Route path="/songsList" component={SongList} />
      </Switch>
    </Router>
  );
}
