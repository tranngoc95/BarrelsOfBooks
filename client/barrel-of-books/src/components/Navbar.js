import { Link } from "react-router-dom";
import AuthContext from '../AuthContext';
import { useContext } from 'react';

function Navbar() {

    const auth = useContext(AuthContext);

    return (
        <>
            <div class="ui secondary menu">
                <Link className="item" to="/">Barrel of Books</Link>
                <Link className="item" to="/books">Books</Link>
                <Link className="item" to="/stores">Stores</Link>
                <Link className="item" to="/genres">Genres</Link>
                <Link className="item" to="/cart">Cart</Link>
                <Link className="item" to="/orders">My Orders</Link>
                {auth.user && auth.user.hasRole('ADMIN') && <Link className="item" to="/allorders">All Orders</Link>}
                <div class="right menu">
                    {!auth.user && (
                        <>
                            <Link className="ui item" to="/login">Login</Link>
                            <Link className="ui item" to="/register">Register</Link>
                        </>
                    )}
                    {auth.user &&
                        <>
                            <span className="ui item"><i>Hello {auth.user.username}!</i></span>
                            <Link className="ui item" to="/login" onClick={() => auth.logout()} >Logout</Link>
                        </>
                    }
                </div>
            </div>
            {/* <nav class="navbar" role="navigation" aria-label="main navigation">
                <div class="navbar-brand">
                    <a class="navbar-item" href="https://bulma.io">
                        <img src="https://bulma.io/images/bulma-logo.png" width="112" height="28"/>
                    </a>

                    <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
                        <span aria-hidden="true"></span>
                        <span aria-hidden="true"></span>
                        <span aria-hidden="true"></span>
                    </a>
                </div>

                <div id="navbarBasicExample" class="navbar-menu">
                    <div class="navbar-start">
                        <a class="navbar-item">
                            Home
                        </a>

                        <a class="navbar-item">
                            Documentation
                        </a>

                        <div class="navbar-item has-dropdown is-hoverable">
                            <a class="navbar-link">
                                More
                            </a>

                            <div class="navbar-dropdown">
                                <a class="navbar-item">
                                    About
                                </a>
                                <a class="navbar-item">
                                    Jobs
                                </a>
                                <a class="navbar-item">
                                    Contact
                                </a>
                                <hr class="navbar-divider"/>
                                <a class ="navbar-item">
                                Report an issue
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="navbar-end">
                        <div class="navbar-item">
                            <div class="buttons">
                                <a class="button is-primary">
                                    <strong>Sign up</strong>
                                </a>
                                <a class="button is-light">
                                    Log in
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </nav> */}
        </>
    )
}

export default Navbar;