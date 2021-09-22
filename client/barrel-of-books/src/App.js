import { useState } from 'react';
import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";
import jwt_decode from 'jwt-decode';

import './App.css';
import AuthContext from "./AuthContext";
import Home from "./components/Home";
import Navbar from "./components/Navbar";
import Login from "./components/Login";
import Register from "./components/Register";
import NotFound from "./components/NotFound";

import Stores from "./components/store/Stores";
import EditStore from "./components/store/EditStore";
import AddStore from "./components/store/AddStore";

import Cart from "./components/order/Cart";
import Orders from "./components/order/Orders";
import CancelOrder from "./components/order/CancelOrder";
import CancelItem from "./components/order/CancelItem";
import Confirmation from "./components/order/Confirmation";

import Genres from "./components/genre/Genres";
import AddGenre from "./components/genre/AddGenre";
import EditGenre from "./components/genre/EditGenre";
import AllOrders from "./components/order/AllOrders";

import Book from './components/book/Book';
import Books from "./components/book/Books";
import AddBook from "./components/book/AddBook";
import EditBook from "./components/book/EditBook";
import BooksSearched from './components/book/BooksSearched';

const GuestRoutes = [
  { path: "/", component: Home },
  { path: "/login", component: Login },
  { path: "/register", component: Register },
  { path: "/books/each/:id", component: Book },
  { path: "/books", component: Books },
  { path: "/books/search/:phrase", component: BooksSearched },
  { path: "/genres", component: Genres },
  { path: "/stores", component: Stores }
]

const UserRoutes = [
  { path: "/cart", component: Cart },
  { path: "/orders", component: Orders },
  { path: "/orders/cancel/:id", component: CancelOrder },
  { path: "/orders/cancel/item/:id", component: CancelItem },
  { path: "/orders/confirmation/:id", component: Confirmation }
]

const ManagerRoutes = [
  { path: "/allorders", component: AllOrders },
  { path: "/books/add/", component: AddBook },
  { path: "/books/edit/:id", component: EditBook }
]

const AdminRoutes = [
  { path: "/stores/add", component: AddStore },
  { path: "/stores/edit/:id", component: EditStore },
  { path: "/genres/add", component: AddGenre },
  { path: "/genres/edit/:id", component: EditGenre }
]

function App() {
  const [user, setUser] = useState(null);

  const login = (token) => {
    const decodedToken = jwt_decode(token);

    const roles = decodedToken.roles.split(',');

    const user = {
      id: decodedToken.id,
      username: decodedToken.sub,
      roles,
      token,
      hasRole(role) {
        return this.roles.includes(role);
      }
    };

    setUser(user);
  }

  const logout = () => {
    setUser(null);
  }

  const auth = {
    user,
    login,
    logout
  };

  return (
    <AuthContext.Provider value={auth}>
      <Router>
        <div className="App">
          <Navbar />

          <Switch>
            {GuestRoutes.map(each => (
              <Route key={each.path} exact path={each.path}>
                <each.component />
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

            {ManagerRoutes.map(each => (
              <Route key={each.path} exact path={each.path}>
                {user ? (user.hasRole('MANAGER') ?
                  <each.component /> : <NotFound />)
                  :
                  <Redirect to={{
                    pathname: '/login',
                    state: { nextpath: each.path }
                  }} />
                }
              </Route>
            ))}

            {AdminRoutes.map(each => (
              <Route key={each.path} exact path={each.path}>
                {user ? (user.hasRole('ADMIN') ?
                  <each.component /> : <NotFound />)
                  :
                  <Redirect to={{
                    pathname: '/login',
                    state: { nextpath: each.path }
                  }} />
                }
              </Route>
            ))}
            <Route path="*">
              <NotFound />
            </Route>

          </Switch>
        </div>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
