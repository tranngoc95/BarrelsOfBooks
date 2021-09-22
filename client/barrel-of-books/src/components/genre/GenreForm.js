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
			<form class="ui small form" ref={formElement}>
				<div class="ui stacked segment">
					<div className="field">
						<label htmlFor="name">Name:</label>
						<input id="name" name="name" value={genre.name} onChange={handleOnChange} type="text" required></input>
					</div>
					<div className="field">
						<label htmlFor="description">Description:</label>
						<textarea id="description" name="description" value={genre.description} onChange={handleOnChange}></textarea>
					</div>
					<div className="ui buttons">
						<button className="ui positive button active" type="submit" onClick={handleSubmit}>Submit</button>
						<div class="or"></div>
						<Link className="ui button" type="button" to='/genres'>Cancel</Link>
					</div>
				</div>
			</form>
		</>
	)
}

export default GenreForm;