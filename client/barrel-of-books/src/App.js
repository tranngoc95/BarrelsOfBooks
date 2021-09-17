import * as React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import './App.css';
import Home from "./components/Home";
import Stores from "./components/Stores";
import EditStore from "./components/EditStore";
function App() {
  return (
    <Router>
        <div className="App">
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/api/stores">
              <Stores />
            </Route>
            <Route>
              <EditStore />
            </Route>
          </Switch>
        </div>
      </Router>
  );
}

export default App;
