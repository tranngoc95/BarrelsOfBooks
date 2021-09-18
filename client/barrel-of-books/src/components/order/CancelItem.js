import { useState, useEffect, useContext } from 'react';
import { Link, useParams, useHistory } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';

function CancelItem() {

    const [errorList, setErrorList] = useState([]);
    const [item, setItem] = useState(null);
    const URL = 'http://localhost:8080/api/cart-item';

    const history = useHistory();

    const { id } = useParams();

    useEffect(() => {
        const init = {
            headers: {
                'Authorization': 'Bearer ${auth.user.token}'
            }
        }

        fetch(URL + `/${id}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    return [`Item with id ${id} does not exist.`];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (data.cartItemId) {
                    setItem(data);
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));
    }, [id]);

    const cancelItem = () => {

        const init = {
            method: "Delete",
            headers: {
                'Authorization': 'Bearer ${auth.user.token}'
            }
        }

        fetch(URL + `/${id}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Item with id ${id} does not exist.`];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (!data) {
                    history.push('/orders');
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));
    }

    return (
        <div>
            <h3>Cancel Item</h3>
            <ErrorMessages errorList={errorList} />
            {item && (
                <>
                    <h5>item.book.title</h5>

                    <hr />
                    <div>Are you sure you want to cancel this item?</div>
                    <div >
                        <button type="button" onClick={cancelItem}>Yes</button>
                        <Link to='/orders'>Cancel</Link>
                    </div>
                </>
            )}
        </div>
    )
}

export default CancelItem;