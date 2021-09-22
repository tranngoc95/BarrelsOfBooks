function ErrorMessages({ errorList = [] }) {

	return (
		<>
			{errorList.length > 0 ?
				(
					<div className="alert alert-danger">
						{errorList.map(error => (
							<li key={error}>{error}</li>
						))}
					</div>
				)
				: null
			}
		</>

	);
}

export default ErrorMessages;