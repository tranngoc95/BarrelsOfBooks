import { useState, useContext } from 'react';
import { useHistory } from 'react-router-dom';

import AuthContext from '../../AuthContext';
import GenreForm from './GenreForm';

function AddGenre() {

	const emptyGenre = {
		"name": "",
		"description": ""
	}

	const [genre, setGenre] = useState(emptyGenre);
	const [errorList, setErrorList] = useState([]);

	const URL = 'http://localhost:8080/api/genre';
	const auth = useContext(AuthContext);
	const history = useHistory();

	const handleOnChange = (event) => {
		const newGenre = { ...genre };
		newGenre[event.target.name] = event.target.value;
		setGenre(newGenre);
	};

	const addGenre = () => {

		const init = {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': `Bearer ${auth.user.token}`
			},
			body: JSON.stringify(genre)
		};

		fetch(URL, init)
			.then(response => {
				if(response.status === 403) {
					return ['You are not authorized to make changes to this record.'];
				}
				if (response.status !== 400 && response.status !== 201) {
					return Promise.reject("Something went wrong. :(")
				}
				return response.json();
			})
			.then(data => {
				if (data.genreId) {
					history.push('/genres');
				} else {
					setErrorList(data);
				}
			})
			.catch(error => console.log('Error:', error));
	}

	return (
		<div className="ui container">
			<h3>Add Genre</h3>
			<GenreForm genre={genre} handleOnChange={handleOnChange} errorList={errorList} action={addGenre} />
		</div>
	)
}

export default AddGenre;