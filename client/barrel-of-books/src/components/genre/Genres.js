import { useContext, useEffect, useState } from 'react';

import { Link } from 'react-router-dom';
import ErrorMessages from '../ErrorMessages';
import AuthContext from '../../AuthContext';

function Genres() {

    const [genres, setGenres] = useState([]);
    const [errorList, setErrorList] = useState([]);

    const URL = 'http://localhost:8080/api/genre';
    const auth = useContext(AuthContext);

    const button = true;

    const getList = () => {
        const init = {
            headers: {
                'Authorization': `Bearer ${auth.user.token}`
            }
        }

        fetch(URL, init)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Genres fetch failed.")
                }
                return response.json();
            })
            .then(data => setGenres(data))
            .catch(error => console.log("Error", error));
    }

    useEffect(getList, []);

    const deleteGenre = (genre) => {
        
        const init = {
            method: "Delete",
            headers: {
                'Authorization':`Bearer ${auth.user.token}`
            }
        }

        fetch(URL + `/${genre.genreId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Genre with id ${genre.genreId} does not exist.`];
                } else if (response.status === 403) {
                    return ['You are not authorized to make changes to this record.'];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (!data) {
                    getList();
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));
    }

    return (
        <>
            <div>
                <h2>Genres List</h2>
                <ErrorMessages errorList={errorList}/>
                <Link className="ui primary button add-button" to='./genres/add'>Add New Genre</Link>
                <table className="ui celled table">
                    <thead>
                        <tr>
                            <th scope="col">Genre</th>
                            <th scope="col">Description</th>
                            {button &&
                            <>
                                <th>&nbsp;</th>
                                <th>&nbsp;</th>
                                </>
                            }
                        </tr>
                    </thead>
                    <tbody>
                        {genres.map(genre => (
                            <tr key={genre.genreId}>
                                <td data-label="Genre">{genre.name}</td>
                                <td data-label="Description">{genre.description}</td>
                                {button &&
                                <>
                                    <td>
                                        <Link className="ui primary button" to={`/genres/edit/${genre.genreId}`}>Update</Link>
                                    </td>
                                    <td>
                                         <button className="ui primary button" type="button" onClick={() => deleteGenre(genre)}>Delete</button>
                                    </td>
                                    </>
                                }
                            </tr>
                        ))
                        }
                    </tbody>
                </table>
                <Link className="ui secondary button" to="/">
      Go Back
    </Link>
            </div>
        </>
    );

}

export default Genres;