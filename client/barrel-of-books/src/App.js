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

const UserRoutes = [
  { path="/cart", component=Cart },
  { path="/orders", component=Orders },
  { path="/stores", component=Stores }
]

const AdminRoutes = [
  { path="/stores/edits", component=EditStore },
]

function App() {
  const user = "placeholder";
  return (
    <Router>
      <div className="App">
        <Home />
        
        <Switch>
          <Route exact path="/">
            <Home />
          </Route>
          {AdminRoutes.map(each => (
            <Route key={each.path} exact path={each.path}>
              {user ?
                <each.component />
                :
                <Redirect to={{
                  pathname: '/login',
                  state: { nextpath: each.path }
                }} />
              }
            </Route>
          ))}

          {UserRoutes.map(each => (
            <Route key={each.path} exact path={each.path}>
              {user ?
                <each.component />
                :
                <Redirect to={{
                  pathname: '/login',
                  state: { nextpath: each.path }
                }} />
              }
            </Route>
          ))}

        </Switch>
      </div>
    </Router>
  );
}

export default App;
