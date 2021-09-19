import { useEffect, useState, useContext } from 'react';
import { useHistory, useParams } from "react-router-dom";

import GenreForm from "./GenreForm";

import AuthContext from '../../AuthContext';

function EditGenre() {

	const emptyGenre = {
		"name": "",
		"description": ""
	}

	const [errorList, setErrorList] = useState([]);
	const [genre, setGenre] = useState(emptyGenre);

	const auth = useContext(AuthContext);
	const URL = 'http://localhost:8080/api/genre';
	const history = useHistory();

	const { id } = useParams();

	useEffect(() => {
		const init = {
			headers: {
				'Authorization': `Bearer ${auth.user.token}`
			}
		}

		fetch(URL + `/${id}`, init)
			.then(response => {
				console.log('response: ' + response);
				if (response.status === 200) {
					return response.json();
				} else if (response.status === 404) {
					return [`Genre with id ${id} does not exist.`];
				}
				return Promise.reject("Something went wrong, sorry :(");
			})
			.then(data => {
				console.log(data);
				if (data.genreId) {
					setGenre(data);
				} else {
					setErrorList(data);
				}
			})
			.catch(error => console.log("Error", error));
	}, [id, auth.user.token]);

	const handleOnChange = (event) => {
		const newGenre = { ...genre };
		newGenre[event.target.name] = event.target.value;
		setGenre(newGenre);
	};

	const editGenre = () => {

		const init = {
			method: "PUT",
			headers: {
				'Content-Type': 'application/json',
				'Authorization': `Bearer ${auth.user.token}`
			},
			body: JSON.stringify(genre)
		};

		fetch(URL + `/${id}`, init)
			.then(response => {
				if (response.status === 204) {
					return null;
				} else if (response.status === 404) {
					return [`Genre with id ${id} does not exist.`];
				} else if (response.status === 400) {
					return response.json();
				}
				return Promise.reject("Something went wrong, sorry :(");
			})
			.then(data => {
				if (!data) {
					history.push('/genres');
				} else {
					setErrorList(data);
				}
			})
			.catch(error => console.log('Error:', error));
	}


	return (
		<div>
			<h3>Edit Genre</h3>
			<GenreForm genre={genre} handleOnChange={handleOnChange} errorList={errorList} action={editGenre} />
		</div>
	)
}

export default EditGenre;