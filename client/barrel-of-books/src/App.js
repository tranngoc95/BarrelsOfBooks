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
import Cart from "./components/Cart";
import Orders from "./components/Orders";
function App() {
  return (
    <Router>
        <div className="App">
        <Home />
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route exact path="/stores">
              <Stores />
            </Route>
            <Route exact path="/stores/edits">
              <EditStore />
            </Route>
            <Route exact path="/cart">
              <Cart/>
            </Route>
            <Route exact path="/orders">
              <Orders/>
            </Route>
          </Switch>
        </div>
      </Router>
  );
}

export default App;
