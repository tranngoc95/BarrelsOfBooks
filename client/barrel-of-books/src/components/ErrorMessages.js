function ErrorMessages({ errorList = [] }) {

	return (
		<>
			{errorList.length > 0 &&
				(
					<div className="ui error message">
						{errorList.map(error => (
							<li key={error}>{error}</li>
						))}
					</div>
				)
			}
		</>

	);
}

export default ErrorMessages;