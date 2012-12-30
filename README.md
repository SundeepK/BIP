# BIP (Batch Image Processor) #

BIP is a easy to use batch image processing application. You can easily apply common effects and adjust HSB values for an image. 
These same settings can then be applied to multiple different images in the same directory and saved.

Current feature:
* Adjust HSB values of an image and apply the same settings to all images in same folder
* Full PNG/JPG support - both read/write
* Multithread support for fast output

Future: 
* GIF and BMP support both read/write
* Common filters such as blur and greyscale
* Select output folder
* Re-work UI to make it more user friendly
* Improve performance 

Bugs: 
*

Performance:
* 100 HD png images : 35-40secs on quad core Q6600 and 4GB ram.