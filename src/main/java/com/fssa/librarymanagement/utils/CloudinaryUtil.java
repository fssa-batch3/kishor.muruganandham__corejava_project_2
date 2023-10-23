package com.fssa.librarymanagement.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryUtil {
	private Cloudinary cloudinary;

	public CloudinaryUtil() {

		// Cloudinary credentials
		final String CLOUDINARY_CLOUD_NAME;
		final String CLOUDINARY_API_KEY;
		final String CLOUDINARY_API_SECRET;

		// Fetch configuration from environment variables
		CLOUDINARY_CLOUD_NAME = System.getenv("CLOUDINARY_CLOUD_NAME");
		CLOUDINARY_API_KEY = System.getenv("CLOUDINARY_API_KEY");
		CLOUDINARY_API_SECRET = System.getenv("CLOUDINARY_API_SECRET");
		
		cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUDINARY_CLOUD_NAME, "api_key",
				CLOUDINARY_API_KEY, "api_secret", CLOUDINARY_API_SECRET));
	}

	public Cloudinary getCloudinary() {
		return cloudinary;
	}
}
