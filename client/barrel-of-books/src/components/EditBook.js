import { useState, useEffect, useContext } from "react";
import { useHistory, Link, useParams } from "react-router-dom";
import ErrorMessages from "./ErrorMessages";
import AuthContext from '../AuthContext';

// reponse.status!==403
function EditBook() {
  const [book, setBook] = useState({
    bookId: 0,
    title: "",
    description: "",
    author: "",
    price: 0,
    quantity: 0,
    genres: [],
    stores: [],
  });
  const [genres, setGenres] = useState([]);
  const [selectedGenres, setSelectedGenres] = useState([]);
  const [stores, setStores] = useState([]);
  const [errorList, setErrorList] = useState([]);
  const history = useHistory();
  const { id } = useParams();
  const auth = useContext(AuthContext);


useEffect(() => {
    const requests = [
    fetch(`http://localhost:8080/api/book/${id}`)
    .then((response) => response.json()),
    fetch("http://localhost:8080/api/genre")
      .then((response) => response.json()),
    fetch("http://localhost:8080/api/store")
      .then((response) => response.json()),
    fetch(`http://localhost:8080/api/genre-book/${id}`)
      .then((response) => response.json()),
    fetch(`http://localhost:8080/api/store-book/${id}`)
      .then((response) => response.json())
    ];

Promise.all(requests)
.then((values) => {
const book = values[0];
const genres = values[1];
const stores = values[2];
const selectedGenres = values[3];
const selectedStores = values[4];


// book from backend doesnt have store array
book.stores = selectedStores; 

stores.forEach((store) => (store.quantity = 0));

selectedStores.forEach((selectedStore) => {
  const storeId = selectedStore.store.storeId;
  const storeIndex = stores.findIndex((s) => s.storeId === storeId);
  stores[storeIndex].quantity = selectedStore.quantity;

});
setBook(book);
setGenres(genres);
setStores(stores);
setSelectedGenres(selectedGenres);

})
.catch(error => console.log(error));

},[id])


  const handleChange = (event) => {
    const newBook = { ...book };
    if (event.target.name === "genres") {
      handleGenres(event);
    } else if (event.target.name === "stores") {
      handleStores(event, newBook);
    } else if (event.target.name === "storeQuantity") {
      handleQuantity(event, newBook);
    } else {
      newBook[event.target.name] = event.target.value;
      setBook(newBook);
    }
  };

  function handleGenres(event) {
    const newGenres = [...selectedGenres];
    const genreId = parseInt(event.target.value, 10);
    const genre = genres.find((genre) => genre.genreId === genreId);
    if (event.target.checked) {
      newGenres.push({bookId: id, genre});
      setSelectedGenres(newGenres);
    } else {
      const genreIndex = newGenres.findIndex((genre) => genre.genre.genreId === genreId);
      if (genreIndex >= 0) {
        newGenres.splice(genreIndex, 1);
        setSelectedGenres(newGenres);
      }
    }
  }

  function handleStores(event, newBook) {
      
    const newBookStores = [...newBook.stores];
    const storeId = parseInt(event.target.value, 10);
    if (event.target.checked) {
      const store = stores.find((store) => store.storeId === storeId);
      newBookStores.push({bookId: id, store, quantity: store.quantity});
      newBook.stores = newBookStores;
      setBook(newBook);
    } else {
      const bookStoreIndex = newBookStores.findIndex(
        (store) => store.store.storeId === storeId
      );
      if (bookStoreIndex >= 0) {
        newBookStores.splice(bookStoreIndex, 1);
        newBook.stores = newBookStores;
        setBook(newBook);

        // zero out quantity
        const storeIndex = stores.findIndex((store) => store.storeId === storeId);
        const newStores = [...stores];
        newStores[storeIndex] = {...newStores[storeIndex], quantity: 0};
        setStores(newStores);

      }
    }
  }

  function handleQuantity(event, newBook) {
    
    const storeId = parseInt(event.target.id, 10);
    const newStores = [...stores];
    const storeIndex = newStores.findIndex(
      (store) => store.storeId === storeId
    );
    newStores[storeIndex] = {
      ...newStores[storeIndex],
      quantity: parseInt(event.target.value, 10),
    };
    setStores(newStores);

    
    const newBookStores = [...newBook.stores];
  
    const bookStoreIndex = newBookStores.findIndex(
      (s) => s.store.storeId === storeId
    );
    newBookStores[bookStoreIndex] = {bookId: id, store: newStores[storeIndex], quantity:parseInt(event.target.value, 10) };
    newBook.stores = newBookStores;
    setBook(newBook);

  }

  const handleSubmit = (event) => {
    event.preventDefault();
    const newBook = {bookId: book.bookId, title: book.title, description: book.description, author: book.author,
                        price: book.price, quantity: book.quantity, genres: selectedGenres, stores: book.stores};

    const init = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        'Authorization': `Bearer ${auth.user.token}`
      },
      body: JSON.stringify(newBook),
    };

    fetch(`http://localhost:8080/api/book/${id}`, init)
      .then((response) => {
        if (
          response.status === 201 ||
          response.status === 204 
        ) {
          return null;
        } else if(response.status === 400 ) {
            return response.json();
        } else if(response.status === 409) {
            return "Book ID does not match the request ID";
        }
        return Promise.reject("Something else went wrong");
      })
      .then((data) => {
        if (!data) {
          handleDeleteStoreBooks();
        } else {
          setErrorList(data);
        }
      })
      .catch((error) => console.log(error));
  };

  const handleDeleteStoreBooks = () => {
    const init = {
      method: "DELETE",
      'Authorization': `Bearer ${auth.user.token}`
    };

    fetch(`http://localhost:8080/api/store-book/${id}`, init)
    .then((response) => {
        if (response.status === 204 || response.status === 200) {
          return null;
        } else if (response.status === 404 || response.status === 400) {
          return null;
        }
        return Promise.reject("Something else went wrong");
      })
      .then((data) => {
        if (!data) {
          handleStoreBook();
        } else {
          setErrorList(data);
        }
      })
      .catch((error) => console.log(error));
  };

  function handleStoreBook() {
    book.stores.map((s) => {
    const storeBook = {bookId: id, store: s.store, quantity: s.quantity};
      const init = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          'Authorization': `Bearer ${auth.user.token}`
        },
        body: JSON.stringify(storeBook),
      };
      fetch("http://localhost:8080/api/store-book", init)
        .then((response) => response.json())
        .then((data) => {
          if (data.bookId) {
            handleDeleteGenreBooks();
          }
        })
        .catch((error) => console.log(error));
    });
  }


  const handleDeleteGenreBooks = () => {
    const init = {
      method: "DELETE",
      'Authorization': `Bearer ${auth.user.token}`
    };
    fetch(`http://localhost:8080/api/genre-book/${id}`, init)
      .then((response) => {
        if (response.status === 204 || response.status === 200) {
          return null;
        } else if (response.status === 404 || response.status === 400) {
          return null;
        }
        return Promise.reject("Something else went wrong");
      })
      .then((data) => {
        if (!data) {
            handleGenreBook();
        } else {
          setErrorList(data);
        }
      })
      .catch((error) => console.log(error));
  };


  function handleGenreBook() {
    selectedGenres.map((genre) => {
      const init = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          'Authorization': `Bearer ${auth.user.token}`
        },
        body: JSON.stringify(genre),
      };
      fetch("http://localhost:8080/api/genre-book", init)
        .then((response) => {
          if (
            response.status === 201 ||
            response.status === 204 ||
            response.status === 400
          ) {
            return response.json();
          }
          return Promise.reject("Something else went wrong");
        })
        .then((data) => {
          if (data.bookId) {
            history.push("/books");
          } else {
            setErrorList(data);
          }
        })
        .catch((error) => console.log(error));
    });
  }


  return (
    <div>
      <h1>edit book</h1>
      <ErrorMessages errorList={errorList} />
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="title">Title</label>
          <input
            type="text"
            id="title"
            name="title"
            value={book.title}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="description">Description</label>
          <textarea
            type="text"
            id="description"
            name="description"
            value={book.description}
            onChange={handleChange}
          />
        </div>

        <div>
          <label htmlFor="author">Author</label>
          <input
            type="text"
            id="author"
            name="author"
            value={book.author}
            onChange={handleChange}
          />
        </div>

        <div>
          <label htmlFor="price">Price $</label>
          <input
            type="number"
            min="1"
            step="any"
            id="price"
            name="price"
            value={book.price}
            onChange={handleChange}
          />
        </div>
        <div>
          <label htmlFor="quantity">Quantity</label>
          <input
            type="number"
            min="1"
            id="quantity"
            name="quantity"
            value={book.quantity}
            onChange={handleChange}
          />
        </div>

        <div>
          <h3>Genres</h3>
          {genres.map((g) => (
            <div key={g.genreId}>
              <input
                type="checkbox"
                value={g.genreId}
                id={g.name}
                name="genres"
                checked={selectedGenres.find((selectedGenre) => selectedGenre.genre.genreId === g.genreId) !== undefined}
                onChange={handleChange}
              />
              <label htmlFor={g.name}>{g.name}</label>
            </div>
          ))}
        </div>

        <div>
          <h3>Stores</h3>
          {stores.map((s) => (
            <div>
              <div key={s.storeId}>
                <input
                  type="checkbox"
                  value={s.storeId}
                  id={s.address}
                  name="stores"
                  checked={
                    book.stores.find((store) => store.store.storeId === s.storeId) !==
                    undefined
                  }
                  onChange={handleChange}
                />
                <label htmlFor={s.address}>{s.address}</label>
              </div>
              <div>
                <label htmlFor={s.storeId}>Quantity</label>
                <input
                  type="number"
                  min="0"
                  id={s.storeId}
                  name="storeQuantity"
                  value={s.quantity}
                  onChange={handleChange}
                  disabled={
                    book.stores.find((store) => store.store.storeId === s.storeId) ===
                    undefined
                  }
                />
              </div>
            </div>
          ))}
        </div>

        <div>
          <button type="submit">Submit</button>
        </div>
      </form>
      <Link className="btn btn-warning" to="/books">
        Go Back
      </Link>
    </div>
  );
}

export default EditBook;
