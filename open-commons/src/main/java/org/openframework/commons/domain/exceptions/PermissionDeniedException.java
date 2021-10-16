package org.openframework.commons.domain.exceptions;

public class PermissionDeniedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1964782610889471050L;
	public PermissionDeniedException() {
        super();
    }
    public PermissionDeniedException( final String s ) {
        super( s );
    }
    public PermissionDeniedException( final String s, final Throwable throwable ) {
        super( s, throwable );
    }
    public PermissionDeniedException( final Throwable throwable ) {
        super( throwable );
    }
}
