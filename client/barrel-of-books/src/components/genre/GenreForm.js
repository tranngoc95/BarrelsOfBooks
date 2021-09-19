import { useRef } from 'react';
import { Link } from 'react-router-dom';
import ErrorMessages from '../ErrorMessages';

function GenreForm({ genre, handleOnChange, errorList, action }) {

	const formElement = useRef(null);

	const handleSubmit = (event) => {

		event.preventDefault();

		if (formElement.current.reportValidity()) {
			action();
		}
	};

	return (
		<>
			<ErrorMessages errorList={errorList} />
			<form ref={formElement}>
				<div>
					<label htmlFor="name">Name:</label>
					<input id="name" name="name" value={genre.name} onChange={handleOnChange} type="text" required></input>
				</div>
				<div>
					<label htmlFor="description">Description:</label>
					<input id="description" name="description" value={genre.description} onChange={handleOnChange} type="textarea"></input>
				</div>
				<div>
					<button type="submit" onClick={handleSubmit}>Submit</button>
					<Link type="button" to='/genres'>Cancel</Link>
				</div>
			</form>
		</>
	)
}

export default GenreForm;