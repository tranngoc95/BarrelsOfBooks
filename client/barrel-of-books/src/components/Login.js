import React, { useState, useContext } from 'react';
import { Link, useHistory, useLocation } from 'react-router-dom';

import AuthContext from '../AuthContext';
import ErrorMessages from './ErrorMessages';

export default function Login() {
	const [username, setUsername] = useState('');
	const [password, setPassword] = useState('');
	const [errorList, setErrorList] = useState([]);

	const auth = useContext(AuthContext);
	const history = useHistory();
	const location = useLocation();

	const handleSubmit = async (event) => {
		event.preventDefault();

		const credentials = {
			username,
			password
		};

		const init = {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(credentials)
		};

		fetch('http://localhost:5000/authenticate', init)
			.then(response => {
				if (response.status === 200) {
					return response.json();
				} else if (response.status === 403) {
					setErrorList(['Login failed.']);
				} else {
					setErrorList(['Unknown error.']);
				}
			})
			.then(data => {
				if (data) {
					const { jwt_token } = data;
					auth.login(jwt_token);
					if (location.state) {
						history.push(location.state.nextpath);
					} else {
						history.push('/');
					}
				}

			})
			.catch(error => console.log(error));
	};

	return (
		<div>
			<h2 >Login</h2>
			<ErrorMessages errorList={errorList} />
			<form onSubmit={handleSubmit}>
				<div>
					<label>Username:</label>
					<input type="text" onChange={(event) => setUsername(event.target.value)} />
				</div>
				<div>
					<label>Password: </label>
					<input type="password" onChange={(event) => setPassword(event.target.value)} />
				</div>
				<div>
					<button type="submit" >Login</button>
					<Link to="/register">I don't have an account</Link>
				</div>
			</form>
		</div>
	);
}