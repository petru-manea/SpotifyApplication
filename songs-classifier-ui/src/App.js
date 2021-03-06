import React from "react";
import { createMuiTheme, ThemeProvider } from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";

import "./App.css";
import HeaderTabs from "./components/Header";

function App() {
  const prefersDarkMode = true;
  const theme = React.useMemo(
    () =>
      createMuiTheme({
        palette: {
          type: prefersDarkMode ? "dark" : "light",
          primary: {
            main: "#1c2841",
          },
          secondary: {
            main: "#637da2",
            contrastText: "#fff",
          },
          default:{main:"#637da2"},
          background: {
            paper: "#637da2",
            default: "#0b121e",
          },
          text: {
            icon: "#fff",
          },
        },
      }),
    [prefersDarkMode]
  );

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
     <HeaderTabs></HeaderTabs>
    </ThemeProvider>
  );
}

export default App;
