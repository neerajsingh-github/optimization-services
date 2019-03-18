
package info;
import java.lang.*;
import java.io.File;

import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSGlobals.DebugLevel;
import com.gams.api.GAMSCheckpoint;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;


import java.util.Date;
import java.util.ArrayList;
import java.security.MessageDigest;
//import com.google.gson.GsonBuilder;


public class JGamsBlockChain {
	public static  ArrayList<Block> blockchain = null;
	private static boolean initBlockChainFlag =false; 
	public static int difficulty = 2;

	
	
	
	//-- This method will need to check the hash variable is actually equal to the calculated hash, and the previous block’s hash is equal to the previousHash variable. --//
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
		}
		return true;
	}
	

	private static String calcHash256(String input) {
		//Applies Sha256 to a string and returns the result.
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
			//Applies sha256 to our input, 
			byte[] hash = digest.digest(input.getBytes("UTF-8"));	        
			StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}//calcHash //
	
	
	/*********************************************************************/
	// Class Block// 
	private class Block {
		
		private long ts;
		private String data;
		private String previousHash;
		private String hash;
		
		private int index;
		private int nonce;
		
		public Block(String data, String prevHash) {
			
			this.ts=new Date().getTime(); 
			this.data=data;
			this.previousHash = prevHash;
			
			//this.index=1;
			//this.nonce=0;
			
			this.hash = calculateHash();
			
		}
		
		
		public String calculateHash() {
			String calculatedhash = JGamsBlockChain.calcHash256( 
					this.previousHash +
					Long.toString(this.ts) +
					Integer.toString(this.nonce) + 
					this.data 
					);
			return calculatedhash;
		}
		
		public void mineBlock(int difficulty) {
			String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
			System.out.println(" mineBlock:  target = " + target);
			
			while(! this.hash.substring( 0, difficulty).equals(target)) {
				this.nonce++;
				this.hash = this.calculateHash();
				
				/////////////////
				//System.out.println(" --> hash substring= " + this.hash.substring( 0, difficulty) );
				//if (nonce == 100) break;
			}
			System.out.println("Block Mined!!! : " + this.hash);
		}
		
		public void displayBlock() {
			System.out.println("    Block { ");
			System.out.println("\t Data: " + this.data);
			System.out.println("\t TS: " + this.ts);
			System.out.println("\t PreviousHash: " + this.previousHash);
			System.out.println("\t Hash: " + this.hash);
			System.out.println("\t Index: " + this.index);
			System.out.println("\t Nonce: " + this.nonce);
			System.out.println("    } ");
		}
		
	}// Block class //
	/*********************************************************************/
	
	
	/*********************************************************************/
	//Process BlockChain Main //
	public void processBlockChain() {
		
		//public static  ArrayList<Block> blockchain = new ArrayList<Block>(); 
		
		
		Block genesisBlock = new Block("Hi in the first block", "0");
		System.out.println("Hash for block 1 : " + genesisBlock.hash);
		
		Block secondBlock = new Block("Yo in the second block",genesisBlock.hash);
		Block thirdBlock = new Block("Hey in the third block",secondBlock.hash);

		if (blockchain == null) {
			blockchain = new ArrayList<Block>();
			blockchain.add(genesisBlock);
			initBlockChainFlag=true;
		}
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);
		System.out.println("\nThe block chain-Block 0: "+ blockchain.get(0)); 
		blockchain.get(0).displayBlock();
		
		blockchain.add(secondBlock);
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);
		System.out.println("\nThe block chain-Block 1: "+ blockchain.get(1));
		blockchain.get(1).displayBlock();
		
		blockchain.add(thirdBlock);
		System.out.println("Trying to Mine block 3... ");
		blockchain.get(2).mineBlock(difficulty); 
		System.out.println("\nThe block chain-Block 2: "+ blockchain.get(2));
		blockchain.get(2).displayBlock();
		
		//String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);		
		//System.out.println(blockchainJson);
		
		System.out.println("\nBlockchain is Valid: " + JGamsBlockChain.isChainValid());
		
		//String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nOver ! ");
		//System.out.println(blockchainJson);

		
	} //Process BlockChain Main //
	/*********************************************************************/
	
}//-- class JGamsProcess --//





